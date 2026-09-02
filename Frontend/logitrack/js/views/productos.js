import { api, ApiError } from '../api.js';
import { toast, openModal, closeModal, confirmDialog, formEl, formError, fieldErrorsToText, escapeHtml, money } from '../ui.js';

export const productosMeta = { title: 'Productos', eyebrow: 'Catálogo' };

let cache = [];
let search = '';

export async function renderProductos(root) {
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
        <h3>Productos</h3>
        <p class="view-sub">Catálogo maestro de artículos gestionados en el inventario.</p>
      </div>
      <div class="view-actions">
        <input id="buscador" class="search-input" type="text" placeholder="Buscar por nombre o categoría…" />
        <button class="btn btn-primary" id="btn-nuevo">+ Nuevo producto</button>
      </div>
    </div>
    <div class="table-wrap">
      <table>
        <thead><tr><th>Nombre</th><th>Categoría</th><th>Precio</th><th></th></tr></thead>
        <tbody id="productos-tbody"><tr class="loading-row"><td colspan="4">Cargando…</td></tr></tbody>
      </table>
    </div>
  `;
}

async function load(root) {
  try {
    cache = await api.get('/productos');
    paint(root);
  } catch (err) {
    toast(err.message, 'error');
    root.querySelector('#productos-tbody').innerHTML = `<tr><td colspan="4" class="row-muted">No se pudieron cargar los productos.</td></tr>`;
  }
}

function paint(root) {
  const tbody = root.querySelector('#productos-tbody');
  const rows = cache.filter(
    (p) => !search || p.nombre.toLowerCase().includes(search) || p.categoria.toLowerCase().includes(search)
  );

  if (!rows.length) {
    tbody.innerHTML = `<tr><td colspan="4"><div class="empty-state"><strong>Sin resultados</strong>Crea un producto o ajusta la búsqueda.</div></td></tr>`;
    return;
  }

  tbody.innerHTML = rows
    .map(
      (p) => `
    <tr data-id="${p.id}">
      <td>${escapeHtml(p.nombre)}</td>
      <td>${escapeHtml(p.categoria)}</td>
      <td class="cell-num">${money(p.precio)}</td>
      <td class="cell-actions">
        <button class="btn btn-ghost btn-sm" data-act="edit">Editar</button>
        <button class="btn btn-danger btn-sm" data-act="del">Eliminar</button>
      </td>
    </tr>`
    )
    .join('');

  tbody.querySelectorAll('[data-act="edit"]').forEach((btn) =>
    btn.addEventListener('click', (e) => openForm(root, cache.find((p) => p.id === Number(e.target.closest('tr').dataset.id))))
  );
  tbody.querySelectorAll('[data-act="del"]').forEach((btn) =>
    btn.addEventListener('click', (e) => remove(root, cache.find((p) => p.id === Number(e.target.closest('tr').dataset.id))))
  );
}

function openForm(root, producto) {
  const isEdit = !!producto;
  const body = formEl(`
    <form novalidate>
      <div class="form-grid">
        <label class="field span-2">
          <span>Nombre</span>
          <input name="nombre" required maxlength="150" value="${escapeHtml(producto?.nombre || '')}" />
        </label>
        <label class="field">
          <span>Categoría</span>
          <input name="categoria" required maxlength="100" value="${escapeHtml(producto?.categoria || '')}" />
        </label>
        <label class="field">
          <span>Precio (COP)</span>
          <input name="precio" type="number" min="0" step="0.01" required value="${producto?.precio ?? ''}" />
        </label>
      </div>
      <div class="form-actions">
        <button type="button" class="btn btn-ghost" data-act="cancel">Cancelar</button>
        <button type="submit" class="btn btn-primary">${isEdit ? 'Guardar cambios' : 'Crear producto'}</button>
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
      categoria: fd.get('categoria').trim(),
      precio: Number(fd.get('precio')),
    };
    const submitBtn = body.querySelector('button[type="submit"]');
    submitBtn.disabled = true;
    try {
      if (isEdit) {
        await api.put(`/productos/${producto.id}`, payload);
        toast('Producto actualizado.');
      } else {
        await api.post('/productos', payload);
        toast('Producto creado.');
      }
      closeModal();
      await load(root);
    } catch (err) {
      submitBtn.disabled = false;
      formError(body, err instanceof ApiError && err.fieldErrors ? fieldErrorsToText(err.fieldErrors) : err.message);
    }
  });

  openModal(isEdit ? 'Editar producto' : 'Nuevo producto', body);
}

async function remove(root, producto) {
  const ok = await confirmDialog({
    title: 'Eliminar producto',
    message: `¿Eliminar "${producto.nombre}"?`,
    sub: 'Si el producto tiene inventario o movimientos asociados, la eliminación fallará.',
    confirmLabel: 'Eliminar',
  });
  if (!ok) return;
  try {
    await api.del(`/productos/${producto.id}`);
    toast('Producto eliminado.');
    await load(root);
  } catch (err) {
    toast(err.message, 'error');
  }
}
