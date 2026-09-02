import { isAdmin } from './state.js';
import { renderDashboard, dashboardMeta } from './views/dashboard.js';
import { renderBodegas, bodegasMeta } from './views/bodegas.js';
import { renderProductos, productosMeta } from './views/productos.js';
import { renderInventario, inventarioMeta } from './views/inventario.js';
import { renderMovimientos, movimientosMeta } from './views/movimientos.js';
import { renderUsuarios, usuariosMeta } from './views/usuarios.js';
import { renderAuditoria, auditoriaMeta } from './views/auditoria.js';

const routes = {
  dashboard: { render: renderDashboard, meta: dashboardMeta, adminOnly: false },
  bodegas: { render: renderBodegas, meta: bodegasMeta, adminOnly: false },
  productos: { render: renderProductos, meta: productosMeta, adminOnly: false },
  inventario: { render: renderInventario, meta: inventarioMeta, adminOnly: false },
  movimientos: { render: renderMovimientos, meta: movimientosMeta, adminOnly: false },
  usuarios: { render: renderUsuarios, meta: usuariosMeta, adminOnly: true },
  auditoria: { render: renderAuditoria, meta: auditoriaMeta, adminOnly: true },
};

const viewRoot = document.getElementById('view-root');
const topbarTitle = document.getElementById('topbar-title');
const topbarEyebrow = document.getElementById('topbar-eyebrow');

let currentToken = 0;

function currentRouteName() {
  const hash = location.hash.replace('#/', '').split('?')[0];
  return routes[hash] ? hash : 'dashboard';
}

export async function resolveRoute() {
  const name = currentRouteName();
  const route = routes[name];

  if (route.adminOnly && !isAdmin()) {
    location.hash = '#/dashboard';
    return;
  }

  document.querySelectorAll('.nav-item').forEach((a) => a.classList.toggle('active', a.dataset.route === name));
  topbarTitle.textContent = route.meta.title;
  topbarEyebrow.textContent = route.meta.eyebrow;

  const token = ++currentToken;
  viewRoot.innerHTML = '';
  try {
    await route.render(viewRoot);
  } catch (err) {
    if (token !== currentToken) return; // el usuario navegó a otra vista mientras cargaba
    viewRoot.innerHTML = `<div class="empty-state"><strong>Ocurrió un error al cargar esta vista</strong>${err.message || ''}</div>`;
  }
}

export function initRouter() {
  window.addEventListener('hashchange', resolveRoute);
  if (!location.hash) location.hash = '#/dashboard';
  resolveRoute();
}
