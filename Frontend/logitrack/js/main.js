import { api, setUnauthorizedHandler, pingApi } from './api.js';
import { getApiBase, setApiBase } from './config.js';
import { loadSession, setSession, clearSession, session, isAdmin } from './state.js';
import { toast, setBusy } from './ui.js';
import { initRouter } from './router.js';

const loginScreen = document.getElementById('login-screen');
const appShell = document.getElementById('app-shell');
const loginForm = document.getElementById('login-form');
const loginError = document.getElementById('login-error');
const loginSubmit = document.getElementById('login-submit');
const apiBaseInput = document.getElementById('api-base-input');
const sidebar = document.querySelector('.sidebar');
const connDot = document.getElementById('conn-dot');
const connLabel = document.getElementById('conn-label');

apiBaseInput.value = getApiBase();
apiBaseInput.addEventListener('change', () => setApiBase(apiBaseInput.value));

function showLogin() {
  loginScreen.hidden = false;
  appShell.hidden = true;
}

function showApp() {
  loginScreen.hidden = true;
  appShell.hidden = false;

  document.getElementById('user-name').textContent = session.nombre || session.email;
  document.getElementById('user-role').textContent = isAdmin() ? 'Administrador' : 'Empleado';
  document.getElementById('user-avatar').textContent = (session.nombre || session.email || '??')
    .split(' ')
    .map((s) => s[0])
    .slice(0, 2)
    .join('')
    .toUpperCase();

  sidebar.classList.toggle('is-admin', isAdmin());
  initRouter();
  checkConnection();
}

async function checkConnection() {
  const ok = await pingApi();
  connDot.className = `conn-dot ${ok ? 'ok' : 'fail'}`;
  connLabel.textContent = ok ? 'API conectada' : 'API sin respuesta';
}

loginForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  loginError.hidden = true;
  setApiBase(apiBaseInput.value);
  setBusy(loginSubmit, true);

  const email = document.getElementById('login-email').value.trim();
  const password = document.getElementById('login-password').value;

  try {
    const res = await api.login({ email, password });
    setSession(res);
    toast(`Bienvenido, ${res.nombre.split(' ')[0]}.`);
    showApp();
  } catch (err) {
    loginError.textContent = err.message || 'No se pudo iniciar sesión.';
    loginError.hidden = false;
  } finally {
    setBusy(loginSubmit, false);
  }
});

document.getElementById('logout-btn').addEventListener('click', () => {
  clearSession();
  location.hash = '';
  showLogin();
});

setUnauthorizedHandler(() => {
  toast('Tu sesión expiró. Vuelve a iniciar sesión.', 'error');
  showLogin();
});

// Colapsa/expande el sidebar en pantallas angostas
document.getElementById('menu-toggle').addEventListener('click', () => {
  sidebar.classList.toggle('open');
});
document.getElementById('sidebar-nav').addEventListener('click', (e) => {
  if (e.target.closest('.nav-item')) sidebar.classList.remove('open');
});

// ---- Arranque ----
if (loadSession()) {
  showApp();
} else {
  showLogin();
}
