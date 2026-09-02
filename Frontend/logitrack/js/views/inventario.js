import { api, ApiError } from '../api.js';
import { toast, openModal, closeModal, confirmDialog, formEl, formError, fieldErrorsToText, escapeHtml } from '../ui.js';

export const inventarioMeta = { title: 'Inventario', eyebrow: 'Existencias' };

let cache = [];
let bodegas = [];
let productos = [];
let search = '';
const UMBRAL_BAJO = 10;

export async function renderInventario(root) {
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
        <h3>Inventario</h3>
        <p class="view-sub">Stock disponible por producto y bodega. Umbral bajo: &lt; ${UMBRAL_BAJO} unidades.</p>
      </div>
      <div class="view-actions">
        <input id="buscador" class="search-input" type="text" placeholder="Buscar por producto o bodega…" />
        <button class="btn btn-primary" id="btn-nuevo">+ Nuevo registro</button>
      </div>
    </div>
    <div class="table-wrap">
      <table>
        <thead><tr><th>Producto</th><th>Bodega</th><th>Stock</th><th></th></tr></thead>
        <tbody id="inv-tbody"><tr class="loading-row"><td colspan="4">Cargando…</td></tr></tbody>
      </table>
    </div>
  `;
}

async function load(root) {
  try {
    [cache, bodegas, productos] = await Promise.all([
      api.get('/inventarios'),
      api.get('/bodegas'),
      api.get('/productos'),
    ]);
    paint(root);
  } catch (err) {
    toast(err.message, 'error');
    root.querySelector('#inv-tbody').innerHTML = `<tr><td colspan="4" class="row-muted">No se pudo cargar el inventario.</td></tr>`;
  }
}

function paint(root) {
  const tbody = root.querySelector('#inv-tbody');
  const rows = cache.filter(
    (i) => !search || i.productoNombre.toLowerCase().includes(search) || i.bodegaNombre.toLowerCase().includes(search)
  );

  if (!rows.length) {
    tbody.innerHTML = `<tr><td colspan="4"><div class="empty-state"><strong>Sin resultados</strong>Registra existencias o ajusta la búsqueda.</div></td></tr>`;
    return;
  }

  tbody.innerHTML = rows
    .map(
      (i) => `
    <tr data-id="${i.id}">
      <td>${escapeHtml(i.productoNombre)}</td>
      <td>${escapeHtml(i.bodegaNombre)}</td>
      <td class="cell-num ${i.stock < UMBRAL_BAJO ? 'stock-low' : 'stock-ok'}">${i.stock}</td>
      <td class="cell-actions">
        <button class="btn btn-ghost btn-sm" data-act="edit">Editar</button>
        <button class="btn btn-danger btn-sm" data-act="del">Eliminar</button>
      </td>
    </tr>`
    )
    .join('');

  tbody.querySelectorAll('[data-act="edit"]').forEach((btn) =>
    btn.addEventListener('click', (e) => openForm(root, cache.find((i) => i.id === Number(e.target.closest('tr').dataset.id))))
  );
  tbody.querySelectorAll('[data-act="del"]').forEach((btn) =>
    btn.addEventListener('click', (e) => remove(root, cache.find((i) => i.id === Number(e.target.closest('tr').dataset.id))))
  );
}

function opciones(list, selectedId) {
  return list
    .map((x) => `<option value="${x.id}" ${x.id === selectedId ? 'selected' : ''}>${escapeHtml(x.nombre)}</option>`)
    .join('');
}

function openForm(root, item) {
  const isEdit = !!item;

  if (!bodegas.length || !productos.length) {
    toast('Crea primero al menos una bodega y un producto.', 'error');
    return;
  }

  const body = formEl(`
    <form novalidate>
      <div class="form-grid">
        <label class="field span-2">
          <span>Producto</span>
          <select name="productoId" required>${opciones(productos, item?.productoId)}</select>
        </label>
        <label class="field span-2">
          <span>Bodega</span>
          <select name="bodegaId" required>${opciones(bodegas, item?.bodegaId)}</select>
        </label>
        <label class="field span-2">
          <span>Stock</span>
          <input name="stock" type="number" min="0" required value="${item?.stock ?? 0}" />
        </label>
      </div>
      <div class="form-actions">
        <button type="button" class="btn btn-ghost" data-act="cancel">Cancelar</button>
        <button type="submit" class="btn btn-primary">${isEdit ? 'Guardar cambios' : 'Crear registro'}</button>
      </div>
    </form>
  `);

  body.querySelector('[data-act="cancel"]').addEventListener('click', closeModal);
  body.addEventListener('submit', async (e) => {
    e.preventDefault();
    formError(body, null);
    const fd = new FormData(body);
    const payload = {
      productoId: Number(fd.get('productoId')),
      bodegaId: Number(fd.get('bodegaId')),
      stock: Number(fd.get('stock')),
    };
    const submitBtn = body.querySelector('button[type="submit"]');
    submitBtn.disabled = true;
    try {
      if (isEdit) {
        await api.put(`/inventarios/${item.id}`, payload);
        toast('Registro actualizado.');
      } else {
        await api.post('/inventarios', payload);
        toast('Registro creado.');
      }
      closeModal();
      await load(root);
    } catch (err) {
      submitBtn.disabled = false;
      formError(body, err instanceof ApiError && err.fieldErrors ? fieldErrorsToText(err.fieldErrors) : err.message);
    }
  });

  openModal(isEdit ? 'Editar existencias' : 'Nuevo registro de inventario', body);
}

async function remove(root, item) {
  const ok = await confirmDialog({
    title: 'Eliminar registro',
    message: `¿Eliminar el inventario de "${item.productoNombre}" en "${item.bodegaNombre}"?`,
    confirmLabel: 'Eliminar',
  });
  if (!ok) return;
  try {
    await api.del(`/inventarios/${item.id}`);
    toast('Registro eliminado.');
    await load(root);
  } catch (err) {
    toast(err.message, 'error');
  }
}
