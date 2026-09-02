import { api, ApiError } from '../api.js';
import { toast, openModal, closeModal, confirmDialog, formEl, formError, fieldErrorsToText, escapeHtml } from '../ui.js';

export const bodegasMeta = { title: 'Bodegas', eyebrow: 'Infraestructura' };

let cache = [];
let search = '';

export async function renderBodegas(root) {
  root.innerHTML = layout();
  root.querySelector('#btn-nueva').addEventListener('click', () => openForm(root));
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
        <h3>Bodegas</h3>
        <p class="view-sub">Ubicaciones físicas donde se almacena el inventario.</p>
      </div>
      <div class="view-actions">
        <input id="buscador" class="search-input" type="text" placeholder="Buscar por nombre o encargado…" />
        <button class="btn btn-primary" id="btn-nueva">+ Nueva bodega</button>
      </div>
    </div>
    <div class="table-wrap">
      <table>
        <thead>
          <tr><th>Nombre</th><th>Ubicación</th><th>Capacidad</th><th>Encargado</th><th></th></tr>
        </thead>
        <tbody id="bodegas-tbody">
          <tr class="loading-row"><td colspan="5">Cargando…</td></tr>
        </tbody>
      </table>
    </div>
  `;
}

async function load(root) {
  try {
    cache = await api.get('/bodegas');
    paint(root);
  } catch (err) {
    toast(err.message, 'error');
    root.querySelector('#bodegas-tbody').innerHTML =
      `<tr><td colspan="5" class="row-muted">No se pudieron cargar las bodegas.</td></tr>`;
  }
}

function paint(root) {
  const tbody = root.querySelector('#bodegas-tbody');
  const rows = cache.filter(
    (b) => !search || b.nombre.toLowerCase().includes(search) || b.encargado.toLowerCase().includes(search)
  );

  if (!rows.length) {
    tbody.innerHTML = `<tr><td colspan="5"><div class="empty-state"><strong>Sin resultados</strong>Crea una bodega o ajusta la búsqueda.</div></td></tr>`;
    return;
  }

  tbody.innerHTML = rows
    .map(
      (b) => `
    <tr data-id="${b.id}">
      <td>${escapeHtml(b.nombre)}</td>
      <td>${escapeHtml(b.ubicacion)}</td>
      <td class="cell-num">${b.capacidad.toLocaleString('es-CO')}</td>
      <td>${escapeHtml(b.encargado)}</td>
      <td class="cell-actions">
        <button class="btn btn-ghost btn-sm" data-act="edit">Editar</button>
        <button class="btn btn-danger btn-sm" data-act="del">Eliminar</button>
      </td>
    </tr>`
    )
    .join('');

  tbody.querySelectorAll('[data-act="edit"]').forEach((btn) =>
    btn.addEventListener('click', (e) => {
      const id = Number(e.target.closest('tr').dataset.id);
      openForm(root, cache.find((b) => b.id === id));
    })
  );
  tbody.querySelectorAll('[data-act="del"]').forEach((btn) =>
    btn.addEventListener('click', (e) => {
      const id = Number(e.target.closest('tr').dataset.id);
      remove(root, cache.find((b) => b.id === id));
    })
  );
}

function openForm(root, bodega) {
  const isEdit = !!bodega;
  const body = formEl(`
    <form novalidate>
      <div class="form-grid">
        <label class="field span-2">
          <span>Nombre</span>
          <input name="nombre" required maxlength="100" value="${escapeHtml(bodega?.nombre || '')}" />
        </label>
        <label class="field span-2">
          <span>Ubicación</span>
          <input name="ubicacion" required maxlength="200" value="${escapeHtml(bodega?.ubicacion || '')}" />
        </label>
        <label class="field">
          <span>Capacidad (unidades)</span>
          <input name="capacidad" type="number" min="1" required value="${bodega?.capacidad ?? ''}" />
        </label>
        <label class="field">
          <span>Encargado</span>
          <input name="encargado" required maxlength="100" value="${escapeHtml(bodega?.encargado || '')}" />
        </label>
      </div>
      <div class="form-actions">
        <button type="button" class="btn btn-ghost" data-act="cancel">Cancelar</button>
        <button type="submit" class="btn btn-primary">${isEdit ? 'Guardar cambios' : 'Crear bodega'}</button>
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
      ubicacion: fd.get('ubicacion').trim(),
      capacidad: Number(fd.get('capacidad')),
      encargado: fd.get('encargado').trim(),
    };
    const submitBtn = body.querySelector('button[type="submit"]');
    submitBtn.disabled = true;
    try {
      if (isEdit) {
        await api.put(`/bodegas/${bodega.id}`, payload);
        toast('Bodega actualizada.');
      } else {
        await api.post('/bodegas', payload);
        toast('Bodega creada.');
      }
      closeModal();
      await load(root);
    } catch (err) {
      submitBtn.disabled = false;
      formError(body, err instanceof ApiError && err.fieldErrors ? fieldErrorsToText(err.fieldErrors) : err.message);
    }
  });

  openModal(isEdit ? 'Editar bodega' : 'Nueva bodega', body);
}

async function remove(root, bodega) {
  const ok = await confirmDialog({
    title: 'Eliminar bodega',
    message: `¿Eliminar "${bodega.nombre}"?`,
    sub: 'Esta acción no se puede deshacer. Si tiene inventario asociado, la eliminación fallará.',
    confirmLabel: 'Eliminar',
  });
  if (!ok) return;
  try {
    await api.del(`/bodegas/${bodega.id}`);
    toast('Bodega eliminada.');
    await load(root);
  } catch (err) {
    toast(err.message, 'error');
  }
}
