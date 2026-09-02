import { api } from '../api.js';
import { toast, openModal, formEl, escapeHtml, dateTime } from '../ui.js';

export const auditoriaMeta = { title: 'Auditoría', eyebrow: 'Trazabilidad interna' };

let cache = [];
let filtroOp = '';
let search = '';

export async function renderAuditoria(root) {
  root.innerHTML = layout();
  root.querySelector('#filtro-op').addEventListener('change', (e) => {
    filtroOp = e.target.value;
    paint(root);
  });
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
        <h3>Auditoría</h3>
        <p class="view-sub">Registro automático de cambios (INSERT / UPDATE / DELETE) generado por la base de datos.</p>
      </div>
      <div class="view-actions">
        <input id="buscador" class="search-input" type="text" placeholder="Buscar por entidad o usuario…" />
        <select id="filtro-op" class="search-input" style="min-width:150px;">
          <option value="">Todas las operaciones</option>
          <option value="INSERT">Inserciones</option>
          <option value="UPDATE">Actualizaciones</option>
          <option value="DELETE">Eliminaciones</option>
        </select>
      </div>
    </div>
    <div class="table-wrap">
      <table>
        <thead><tr><th>Fecha</th><th>Operación</th><th>Entidad</th><th>Usuario</th><th></th></tr></thead>
        <tbody id="aud-tbody"><tr class="loading-row"><td colspan="5">Cargando…</td></tr></tbody>
      </table>
    </div>
  `;
}

async function load(root) {
  try {
    cache = await api.get('/auditorias');
    paint(root);
  } catch (err) {
    toast(err.message, 'error');
    root.querySelector('#aud-tbody').innerHTML = `<tr><td colspan="5" class="row-muted">No se pudo cargar la auditoría.</td></tr>`;
  }
}

function paint(root) {
  const tbody = root.querySelector('#aud-tbody');
  const rows = [...cache]
    .filter((a) => !filtroOp || a.operacion === filtroOp)
    .filter((a) => !search || a.entidad.toLowerCase().includes(search) || (a.usuarioNombre || '').toLowerCase().includes(search))
    .sort((a, b) => new Date(b.fecha) - new Date(a.fecha));

  if (!rows.length) {
    tbody.innerHTML = `<tr><td colspan="5"><div class="empty-state"><strong>Sin registros</strong>Ajusta los filtros de búsqueda.</div></td></tr>`;
    return;
  }

  tbody.innerHTML = rows
    .map(
      (a) => `
    <tr data-id="${a.id}">
      <td>${dateTime(a.fecha)}</td>
      <td><span class="badge badge-${a.operacion.toLowerCase()}">${a.operacion}</span></td>
      <td>${escapeHtml(a.entidad)}</td>
      <td>${escapeHtml(a.usuarioNombre || 'Sistema')}</td>
      <td class="cell-actions"><button class="btn btn-ghost btn-sm" data-act="ver">Ver cambios</button></td>
    </tr>`
    )
    .join('');

  tbody.querySelectorAll('[data-act="ver"]').forEach((btn) =>
    btn.addEventListener('click', (e) => verDetalle(cache.find((a) => a.id === Number(e.target.closest('tr').dataset.id))))
  );
}

function verDetalle(a) {
  const body = formEl(`
    <div>
      <dl class="kv">
        <dt>Fecha</dt><dd>${dateTime(a.fecha)}</dd>
        <dt>Operación</dt><dd><span class="badge badge-${a.operacion.toLowerCase()}">${a.operacion}</span></dd>
        <dt>Entidad</dt><dd>${escapeHtml(a.entidad)}</dd>
        <dt>Usuario</dt><dd>${escapeHtml(a.usuarioNombre || 'Sistema')}</dd>
      </dl>
      <h4 style="margin: 16px 0 6px; font-size:13px; color: var(--text-muted);">Valores anteriores</h4>
      <pre style="white-space:pre-wrap; background:var(--ink); border:1px solid var(--border); border-radius:4px; padding:10px 12px; font-family:var(--font-mono); font-size:12px; margin:0 0 14px;">${escapeHtml(a.valoresAnteriores || '—')}</pre>
      <h4 style="margin: 0 0 6px; font-size:13px; color: var(--text-muted);">Valores nuevos</h4>
      <pre style="white-space:pre-wrap; background:var(--ink); border:1px solid var(--border); border-radius:4px; padding:10px 12px; font-family:var(--font-mono); font-size:12px; margin:0;">${escapeHtml(a.valoresNuevos || '—')}</pre>
    </div>
  `);
  openModal(`Auditoría #${a.id}`, body);
}
