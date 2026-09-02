// Configuración de LogiTrack Frontend
//
// El backend Spring Boot (SecurityConfig/CorsConfig) sólo acepta peticiones
// CORS desde http://localhost:3000 o http://localhost:5173. Sirve esta carpeta
// desde uno de esos dos orígenes (por ejemplo `npx serve -l 5173`), o agrega
// el origen que uses a CorsConfig.java en el backend.

const STORAGE_KEY = 'logitrack.apiBase';
const DEFAULT_API_BASE = 'http://localhost:8080/api';

export function getApiBase() {
  return localStorage.getItem(STORAGE_KEY) || DEFAULT_API_BASE;
}

export function setApiBase(url) {
  const clean = (url || '').trim().replace(/\/+$/, '');
  if (clean) {
    localStorage.setItem(STORAGE_KEY, clean);
  } else {
    localStorage.removeItem(STORAGE_KEY);
  }
}

export { DEFAULT_API_BASE };
