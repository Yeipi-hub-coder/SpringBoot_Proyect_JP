import { getApiBase } from './config.js';
import { session, clearSession } from './state.js';

export class ApiError extends Error {
  constructor(message, status, fieldErrors) {
    super(message);
    this.status = status;
    this.fieldErrors = fieldErrors || null;
  }
}

// Se dispara cuando cualquier llamada recibe 401 (token vencido/ inválido)
let onUnauthorized = () => {};
export function setUnauthorizedHandler(fn) {
  onUnauthorized = fn;
}

async function request(path, { method = 'GET', body, auth = true } = {}) {
  const headers = { 'Content-Type': 'application/json' };
  if (auth && session.token) {
    headers['Authorization'] = `Bearer ${session.token}`;
  }

  let res;
  try {
    res = await fetch(`${getApiBase()}${path}`, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    });
  } catch (networkErr) {
    throw new ApiError(
      'No se pudo contactar la API. Revisa que el backend esté corriendo y que la URL configurada sea correcta (revisa también la configuración CORS).',
      0
    );
  }

  if (res.status === 204) return null;

  let data = null;
  const text = await res.text();
  if (text) {
    try {
      data = JSON.parse(text);
    } catch (e) {
      data = null;
    }
  }

  if (!res.ok) {
    if (res.status === 401) {
      clearSession();
      onUnauthorized();
    }

    if (data && typeof data === 'object') {
      if (typeof data.error === 'string') {
        throw new ApiError(data.error, res.status);
      }
      // Mapa de errores de validación { campo: mensaje }
      const values = Object.values(data);
      if (values.length && values.every((v) => typeof v === 'string')) {
        throw new ApiError('Revisa los campos marcados.', res.status, data);
      }
    }

    throw new ApiError(
      res.status === 403
        ? 'No tienes permisos para realizar esta acción.'
        : `Error inesperado (${res.status}).`,
      res.status
    );
  }

  return data;
}

export const api = {
  get: (path) => request(path),
  post: (path, body) => request(path, { method: 'POST', body }),
  put: (path, body) => request(path, { method: 'PUT', body }),
  del: (path) => request(path, { method: 'DELETE' }),
  login: (body) => request('/auth/login', { method: 'POST', body, auth: false }),
};

export async function pingApi() {
  try {
    // Cualquier respuesta HTTP (incluso 401/403) confirma que la API responde.
    await fetch(`${getApiBase()}/auth/login`, { method: 'OPTIONS' });
    return true;
  } catch (e) {
    return false;
  }
}
