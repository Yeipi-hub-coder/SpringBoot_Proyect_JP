import { api } from '../api.js';
import { toast, dateTime } from '../ui.js';

export const dashboardMeta = { title: 'Tablero', eyebrow: 'Operación' };

export async function renderDashboard(root) {
  root.innerHTML = `
    <div class="kpi-grid" id="kpi-grid">
      ${skeletonKpi()}${skeletonKpi()}${skeletonKpi()}${skeletonKpi()}
    </div>
    <div class="panel-grid">
      <section class="panel">
        <div class="panel-head"><h3>Stock por bodega</h3></div>
        <div class="panel-body" id="stock-panel">
          <p class="row-muted">Cargando…</p>
        </div>
      </section>
      <section class="panel">
        <div class="panel-head"><h3>Últimos movimientos</h3></div>
        <div class="panel-body">
          <div class="table-wrap" style="border:none;">
            <table>
              <thead>
                <tr><th>Fecha</th><th>Tipo</th><th>Responsable</th><th>Bodegas</th><th>Ítems</th></tr>
              </thead>
              <tbody id="mov-panel">
                <tr class="loading-row"><td colspan="5">Cargando…</td></tr>
              </tbody>
            </table>
          </div>
        </div>
      </section>
    </div>
  `;

  try {
    const [bodegas, productos, inventarios, movimientos] = await Promise.all([
      api.get('/bodegas'),
      api.get('/productos'),
      api.get('/inventarios'),
      api.get('/movimientos'),
    ]);

    renderKpis(root, { bodegas, productos, inventarios, movimientos });
    renderStockPorBodega(root, { bodegas, inventarios });
    renderUltimosMovimientos(root, movimientos);
  } catch (err) {
    toast(err.message || 'No se pudo cargar el tablero.', 'error');
    root.querySelector('#kpi-grid').innerHTML =
      '<div class="empty-state" style="grid-column: 1 / -1;">No se pudieron cargar los indicadores.</div>';
  }
}

function skeletonKpi() {
  return `<div class="kpi"><div class="kpi-value">—</div><div class="kpi-label">Cargando…</div></div>`;
}

function renderKpis(root, { bodegas, productos, inventarios, movimientos }) {
  const stockTotal = inventarios.reduce((sum, i) => sum + (i.stock || 0), 0);
  const hoy = new Date();
  const movimientosHoy = movimientos.filter((m) => {
    const f = new Date(m.fecha);
    return f.toDateString() === hoy.toDateString();
  }).length;

  root.querySelector('#kpi-grid').innerHTML = `
    <div class="kpi">
      <div class="kpi-value">${bodegas.length}</div>
      <div class="kpi-label">Bodegas activas</div>
    </div>
    <div class="kpi kpi-info">
      <div class="kpi-value">${productos.length}</div>
      <div class="kpi-label">Productos catalogados</div>
    </div>
    <div class="kpi kpi-success">
      <div class="kpi-value">${stockTotal.toLocaleString('es-CO')}</div>
      <div class="kpi-label">Unidades en stock</div>
    </div>
    <div class="kpi ${movimientosHoy ? 'kpi-danger' : ''}">
      <div class="kpi-value">${movimientosHoy}</div>
      <div class="kpi-label">Movimientos hoy</div>
    </div>
  `;
}

function renderStockPorBodega(root, { bodegas, inventarios }) {
  const el = root.querySelector('#stock-panel');
  if (!bodegas.length) {
    el.innerHTML = '<div class="empty-state"><strong>Sin bodegas</strong>Crea una bodega para empezar.</div>';
    return;
  }
  const porBodega = bodegas.map((b) => ({
    nombre: b.nombre,
    stock: inventarios.filter((i) => i.bodegaId === b.id).reduce((s, i) => s + (i.stock || 0), 0),
  }));
  const max = Math.max(1, ...porBodega.map((b) => b.stock));

  el.innerHTML = porBodega
    .map(
      (b) => `
      <div class="bar-row">
        <span class="bar-label" title="${b.nombre}">${b.nombre}</span>
        <span class="bar-track"><span class="bar-fill" style="width:${(b.stock / max) * 100}%"></span></span>
        <span class="bar-value">${b.stock}</span>
      </div>`
    )
    .join('');
}

function renderUltimosMovimientos(root, movimientos) {
  const tbody = root.querySelector('#mov-panel');
  const recientes = [...movimientos]
    .sort((a, b) => new Date(b.fecha) - new Date(a.fecha))
    .slice(0, 6);

  if (!recientes.length) {
    tbody.innerHTML = `<tr><td colspan="5" class="row-muted">Aún no hay movimientos registrados.</td></tr>`;
    return;
  }

  tbody.innerHTML = recientes
    .map((m) => {
      const bodegas = [m.bodegaOrigenNombre, m.bodegaDestinoNombre].filter(Boolean).join(' → ') || '—';
      const items = (m.detalles || []).reduce((s, d) => s + d.cantidad, 0);
      return `
      <tr>
        <td>${dateTime(m.fecha)}</td>
        <td><span class="badge badge-${m.tipo.toLowerCase()}">${m.tipo}</span></td>
        <td>${m.usuarioNombre || '—'}</td>
        <td>${bodegas}</td>
        <td class="cell-num">${items}</td>
      </tr>`;
    })
    .join('');
}
