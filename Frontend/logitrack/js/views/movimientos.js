import { api, ApiError } from '../api.js';
import { toast, openModal, closeModal, formEl, formError, fieldErrorsToText, escapeHtml, dateTime } from '../ui.js';

export const movimientosMeta = { title: 'Movimientos', eyebrow: 'Trazabilidad' };

let cache = [];
let bodegas = [];
let productos = [];
let filtroTipo = '';

export async function renderMovimientos(root) {
  root.innerHTML = layout();
  root.querySelector('#btn-nuevo').addEventListener('click', () => openForm(root));
  root.querySelector('#filtro-tipo').addEventListener('change', (e) => {
    filtroTipo = e.target.value;
    paint(root);
  });
  await load(root);
}

function layout() {
  return `
    <div class="view-head">
      <div>
        <h3>Movimientos</h3>
        <p class="view-sub">Historial de entradas, salidas y transferencias. No se pueden editar ni eliminar una vez creados.</p>
      </div>
      <div class="view-actions">
        <select id="filtro-tipo" class="search-input" style="min-width:160px;">
          <option value="">Todos los tipos</option>
          <option value="ENTRADA">Entrada</option>
          <option value="SALIDA">Salida</option>
          <option value="TRANSFERENCIA">Transferencia</option>
        </select>
        <button class="btn btn-primary" id="btn-nuevo">+ Registrar movimiento</button>
      </div>
    </div>
    <div class="table-wrap">
      <table>
        <thead><tr><th>Fecha</th><th>Tipo</th><th>Responsable</th><th>Origen</th><th>Destino</th><th>Ítems</th><th></th></tr></thead>
        <tbody id="mov-tbody"><tr class="loading-row"><td colspan="7">Cargando…</td></tr></tbody>
      </table>
    </div>
  `;
}

async function load(root) {
  try {
    [cache, bodegas, productos] = await Promise.all([
      api.get('/movimientos'),
      api.get('/bodegas'),
      api.get('/productos'),
    ]);
    paint(root);
  } catch (err) {
    toast(err.message, 'error');
    root.querySelector('#mov-tbody').innerHTML = `<tr><td colspan="7" class="row-muted">No se pudieron cargar los movimientos.</td></tr>`;
  }
}

function paint(root) {
  const tbody = root.querySelector('#mov-tbody');
  const rows = [...cache]
    .filter((m) => !filtroTipo || m.tipo === filtroTipo)
    .sort((a, b) => new Date(b.fecha) - new Date(a.fecha));

  if (!rows.length) {
    tbody.innerHTML = `<tr><td colspan="7"><div class="empty-state"><strong>Sin movimientos</strong>Registra una entrada, salida o transferencia.</div></td></tr>`;
    return;
  }

  tbody.innerHTML = rows
    .map((m) => {
      const items = (m.detalles || []).reduce((s, d) => s + d.cantidad, 0);
      return `
    <tr data-id="${m.id}">
      <td>${dateTime(m.fecha)}</td>
      <td><span class="badge badge-${m.tipo.toLowerCase()}">${m.tipo}</span></td>
      <td>${escapeHtml(m.usuarioNombre || '—')}</td>
      <td>${escapeHtml(m.bodegaOrigenNombre || '—')}</td>
      <td>${escapeHtml(m.bodegaDestinoNombre || '—')}</td>
      <td class="cell-num">${items}</td>
      <td class="cell-actions"><button class="btn btn-ghost btn-sm" data-act="ver">Ver detalle</button></td>
    </tr>`;
    })
    .join('');

  tbody.querySelectorAll('[data-act="ver"]').forEach((btn) =>
    btn.addEventListener('click', (e) => verDetalle(cache.find((m) => m.id === Number(e.target.closest('tr').dataset.id))))
  );
}

function verDetalle(m) {
  const body = formEl(`
    <div>
      <dl class="kv">
        <dt>Fecha</dt><dd>${dateTime(m.fecha)}</dd>
        <dt>Tipo</dt><dd><span class="badge badge-${m.tipo.toLowerCase()}">${m.tipo}</span></dd>
        <dt>Responsable</dt><dd>${escapeHtml(m.usuarioNombre || '—')}</dd>
        <dt>Bodega origen</dt><dd>${escapeHtml(m.bodegaOrigenNombre || '—')}</dd>
        <dt>Bodega destino</dt><dd>${escapeHtml(m.bodegaDestinoNombre || '—')}</dd>
      </dl>
      <h4 style="margin: 16px 0 10px; font-size:13px; color: var(--text-muted);">Productos</h4>
      <div class="detail-list">
        ${(m.detalles || [])
          .map((d) => `<div class="detail-item"><span>${escapeHtml(d.productoNombre)}</span><span class="cell-num">${d.cantidad} u.</span></div>`)
          .join('') || '<p class="row-muted">Sin ítems.</p>'}
      </div>
    </div>
  `);
  openModal(`Movimiento #${m.id}`, body);
}

function opciones(list, selectedId) {
  return list
    .map((x) => `<option value="${x.id}" ${x.id === selectedId ? 'selected' : ''}>${escapeHtml(x.nombre)}</option>`)
    .join('');
}

function openForm(root) {
  if (!bodegas.length || !productos.length) {
    toast('Crea primero al menos una bodega y un producto.', 'error');
    return;
  }

  const body = formEl(`
    <form novalidate>
      <div class="form-grid">
        <label class="field span-2">
          <span>Tipo de movimiento</span>
          <select name="tipo" required>
            <option value="ENTRADA">Entrada</option>
            <option value="SALIDA">Salida</option>
            <option value="TRANSFERENCIA">Transferencia</option>
          </select>
        </label>
        <label class="field" data-field="origen">
          <span>Bodega origen</span>
          <select name="bodegaOrigenId">${opciones(bodegas)}</select>
        </label>
        <label class="field" data-field="destino">
          <span>Bodega destino</span>
          <select name="bodegaDestinoId">${opciones(bodegas)}</select>
        </label>
      </div>

      <h4 style="margin: 18px 0 8px; font-size:13px; color: var(--text-muted);">Productos</h4>
      <div id="detalles-wrap"></div>
      <button type="button" class="add-line" id="add-detalle">+ Agregar producto</button>

      <div class="form-actions">
        <button type="button" class="btn btn-ghost" data-act="cancel">Cancelar</button>
        <button type="submit" class="btn btn-primary">Registrar movimiento</button>
      </div>
    </form>
  `);

  const tipoSelect = body.querySelector('[name="tipo"]');
  const origenField = body.querySelector('[data-field="origen"]');
  const destinoField = body.querySelector('[data-field="destino"]');
  const detallesWrap = body.querySelector('#detalles-wrap');

  function syncBodegaFields() {
    const tipo = tipoSelect.value;
    origenField.style.display = tipo === 'ENTRADA' ? 'none' : '';
    destinoField.style.display = tipo === 'SALIDA' ? 'none' : '';
  }
  tipoSelect.addEventListener('change', syncBodegaFields);
  syncBodegaFields();

  function addDetalleRow() {
    const row = document.createElement('div');
    row.className = 'detalle-row';
    row.innerHTML = `
      <select name="detalleProducto">${opciones(productos)}</select>
      <input name="detalleCantidad" type="number" min="1" placeholder="Cant." value="1" required />
      <button type="button" class="detalle-remove" title="Quitar">✕</button>
    `;
    row.querySelector('.detalle-remove').addEventListener('click', () => {
      if (detallesWrap.children.length > 1) row.remove();
    });
    detallesWrap.appendChild(row);
  }
  addDetalleRow();
  body.querySelector('#add-detalle').addEventListener('click', addDetalleRow);

  body.querySelector('[data-act="cancel"]').addEventListener('click', closeModal);
  body.addEventListener('submit', async (e) => {
    e.preventDefault();
    formError(body, null);

    const tipo = tipoSelect.value;
    const detalles = [...detallesWrap.children].map((row) => ({
      productoId: Number(row.querySelector('[name="detalleProducto"]').value),
      cantidad: Number(row.querySelector('[name="detalleCantidad"]').value),
    }));

    const payload = {
      tipo,
      bodegaOrigenId: tipo === 'ENTRADA' ? null : Number(body.querySelector('[name="bodegaOrigenId"]').value),
      bodegaDestinoId: tipo === 'SALIDA' ? null : Number(body.querySelector('[name="bodegaDestinoId"]').value),
      detalles,
    };

    const submitBtn = body.querySelector('button[type="submit"]');
    submitBtn.disabled = true;
    try {
      await api.post('/movimientos', payload);
      toast('Movimiento registrado.');
      closeModal();
      await load(root);
    } catch (err) {
      submitBtn.disabled = false;
      formError(body, err instanceof ApiError && err.fieldErrors ? fieldErrorsToText(err.fieldErrors) : err.message);
    }
  });

  openModal('Registrar movimiento', body);
}
