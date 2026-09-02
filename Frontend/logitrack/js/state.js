// Estado de sesión: token JWT + datos de usuario derivados del payload.

const SESSION_KEY = 'logitrack.session';

function decodeJwtPayload(token) {
  try {
    const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    const json = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + c.charCodeAt(0).toString(16).padStart(2, '0'))
        .join('')
    );
    return JSON.parse(json);
  } catch (err) {
    return null;
  }
}

export const session = {
  token: null,
  usuarioId: null,
  nombre: null,
  email: null,
  rol: null,
  exp: null,
};

export function loadSession() {
  const raw = localStorage.getItem(SESSION_KEY);
  if (!raw) return false;
  try {
    const data = JSON.parse(raw);
    if (!data.token) return false;
    if (data.exp && Date.now() >= data.exp * 1000) {
      clearSession();
      return false;
    }
    Object.assign(session, data);
    return true;
  } catch (err) {
    return false;
  }
}

export function setSession(loginResponse) {
  const payload = decodeJwtPayload(loginResponse.token) || {};
  session.token = loginResponse.token;
  session.usuarioId = loginResponse.usuarioId;
  session.nombre = loginResponse.nombre;
  session.email = loginResponse.email;
  session.rol = payload.rol || null;
  session.exp = payload.exp || null;
  localStorage.setItem(SESSION_KEY, JSON.stringify(session));
}

export function clearSession() {
  session.token = null;
  session.usuarioId = null;
  session.nombre = null;
  session.email = null;
  session.rol = null;
  session.exp = null;
  localStorage.removeItem(SESSION_KEY);
}

export function isAdmin() {
  return session.rol === 'ADMIN';
}

export function isAuthenticated() {
  return !!session.token;
}
