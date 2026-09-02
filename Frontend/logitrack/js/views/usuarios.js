import { api, ApiError } from '../api.js';
import { toast, openModal, closeModal, confirmDialog, formEl, formError, fieldErrorsToText, escapeHtml } from '../ui.js';
import { session } from '../state.js';

export const usuariosMeta = { title: 'Usuarios', eyebrow: 'Administración' };

let cache = [];
let search = '';

export async function renderUsuarios(root) {
  root.innerHTML = layout();
  root.querySelector('#btn-nuevo').addEventListener('click', () => openForm(root));
  root.querySelector('#buscador').addEventListener('input', (e) => {
    search = e.target.value.toLowerCase();
    paint(root);
  });
  await load(root);
}

function layout() {
  return `
    <div class="view-head">
      <div>
        <h3>Usuarios</h3>
        <p class="view-sub">Cuentas con acceso al sistema y su rol asignado.</p>
      </div>
      <div class="view-actions">
        <input id="buscador" class="search-input" type="text" placeholder="Buscar por nombre o correo…" />
        <button class="btn btn-primary" id="btn-nuevo">+ Nuevo usuario</button>
      </div>
    </div>
    <div class="table-wrap">
      <table>
        <thead><tr><th>Nombre</th><th>Correo</th><th>Rol</th><th></th></tr></thead>
        <tbody id="usr-tbody"><tr class="loading-row"><td colspan="4">Cargando…</td></tr></tbody>
      </table>
    </div>
  `;
}

async function load(root) {
  try {
    cache = await api.get('/usuarios');
    paint(root);
  } catch (err) {
    toast(err.message, 'error');
    root.querySelector('#usr-tbody').innerHTML = `<tr><td colspan="4" class="row-muted">No se pudieron cargar los usuarios.</td></tr>`;
  }
}

function paint(root) {
  const tbody = root.querySelector('#usr-tbody');
  const rows = cache.filter(
    (u) => !search || u.nombre.toLowerCase().includes(search) || u.email.toLowerCase().includes(search)
  );

  if (!rows.length) {
    tbody.innerHTML = `<tr><td colspan="4"><div class="empty-state"><strong>Sin resultados</strong>Crea un usuario o ajusta la búsqueda.</div></td></tr>`;
    return;
  }

  tbody.innerHTML = rows
    .map(
      (u) => `
    <tr data-id="${u.id}">
      <td>${escapeHtml(u.nombre)}</td>
      <td>${escapeHtml(u.email)}</td>
      <td><span class="badge badge-${u.rol.toLowerCase()}">${u.rol}</span></td>
      <td class="cell-actions">
        <button class="btn btn-ghost btn-sm" data-act="edit">Editar</button>
        <button class="btn btn-danger btn-sm" data-act="del" ${u.id === session.usuarioId ? 'disabled title="No puedes eliminar tu propia cuenta"' : ''}>Eliminar</button>
      </td>
    </tr>`
    )
    .join('');

  tbody.querySelectorAll('[data-act="edit"]').forEach((btn) =>
    btn.addEventListener('click', (e) => openForm(root, cache.find((u) => u.id === Number(e.target.closest('tr').dataset.id))))
  );
  tbody.querySelectorAll('[data-act="del"]:not([disabled])').forEach((btn) =>
    btn.addEventListener('click', (e) => remove(root, cache.find((u) => u.id === Number(e.target.closest('tr').dataset.id))))
  );
}

function openForm(root, usuario) {
  const isEdit = !!usuario;
  const body = formEl(`
    <form novalidate>
      <div class="form-grid">
        <label class="field span-2">
          <span>Nombre</span>
          <input name="nombre" required maxlength="100" value="${escapeHtml(usuario?.nombre || '')}" />
        </label>
        <label class="field span-2">
          <span>Correo electrónico</span>
          <input name="email" type="email" required maxlength="150" value="${escapeHtml(usuario?.email || '')}" />
        </label>
        <label class="field">
          <span>Rol</span>
          <select name="rol">
            <option value="EMPLEADO" ${usuario?.rol === 'EMPLEADO' ? 'selected' : ''}>Empleado</option>
            <option value="ADMIN" ${usuario?.rol === 'ADMIN' ? 'selected' : ''}>Administrador</option>
          </select>
        </label>
        <label class="field">
          <span>Contraseña</span>
          <input name="password" type="password" minlength="6" maxlength="255" required placeholder="${isEdit ? 'Requerida para guardar cambios' : 'Mínimo 6 caracteres'}" />
        </label>
      </div>
      ${isEdit ? '<p class="field-hint">La API exige reingresar la contraseña para actualizar cualquier dato del usuario.</p>' : ''}
      <div class="form-actions">
        <button type="button" class="btn btn-ghost" data-act="cancel">Cancelar</button>
        <button type="submit" class="btn btn-primary">${isEdit ? 'Guardar cambios' : 'Crear usuario'}</button>
      </div>
    </form>
  `);

  body.querySelector('[data-act="cancel"]').addEventListener('click', closeModal);
  body.addEventListener('submit', async (e) => {
    e.preventDefault();
    formError(body, null);
    const fd = new FormData(body);
    const payload = {
      nombre: fd.get('nombre').trim(),
      email: fd.get('email').trim(),
      password: fd.get('password'),
      rol: fd.get('rol'),
    };
    const submitBtn = body.querySelector('button[type="submit"]');
    submitBtn.disabled = true;
    try {
      if (isEdit) {
        await api.put(`/usuarios/${usuario.id}`, payload);
        toast('Usuario actualizado.');
      } else {
        await api.post('/usuarios', payload);
        toast('Usuario creado.');
      }
      closeModal();
      await load(root);
    } catch (err) {
      submitBtn.disabled = false;
      formError(body, err instanceof ApiError && err.fieldErrors ? fieldErrorsToText(err.fieldErrors) : err.message);
    }
  });

  openModal(isEdit ? 'Editar usuario' : 'Nuevo usuario', body);
}

async function remove(root, usuario) {
  const ok = await confirmDialog({
    title: 'Eliminar usuario',
    message: `¿Eliminar a "${usuario.nombre}"?`,
    sub: 'Perderá acceso al sistema de inmediato.',
    confirmLabel: 'Eliminar',
  });
  if (!ok) return;
  try {
    await api.del(`/usuarios/${usuario.id}`);
    toast('Usuario eliminado.');
    await load(root);
  } catch (err) {
    toast(err.message, 'error');
  }
}
