# LogiTrack — Frontend

Frontend estático (HTML + CSS + JS puro, sin frameworks ni build step) para el backend
Spring Boot `Proyecto_SpringBoot_JP` (sistema de gestión de bodegas, productos,
inventario, movimientos, usuarios y auditoría de **LogiTrack**).

## Contenido

```
logitrack/
├── index.html            Login + estructura de la app (sidebar, topbar, modal)
├── css/styles.css         Sistema de diseño (tokens de color, tipografía, componentes)
└── js/
    ├── main.js             Arranque: login, sesión, logout
    ├── router.js           Router por hash (#/dashboard, #/bodegas, …) y control de acceso por rol
    ├── api.js              Cliente fetch con JWT y manejo de errores del backend
    ├── state.js             Sesión (token, usuario, rol) persistida en localStorage
    ├── config.js            URL base de la API (editable desde el login)
    ├── ui.js                Toasts, modal genérico, confirmaciones, formateo
    └── views/
        ├── dashboard.js      KPIs, stock por bodega, últimos movimientos
        ├── bodegas.js        CRUD de bodegas
        ├── productos.js      CRUD de productos
        ├── inventario.js     CRUD de existencias (stock por bodega/producto)
        ├── movimientos.js    Registro de entradas/salidas/transferencias + detalle (solo lectura tras crear)
        ├── usuarios.js       CRUD de usuarios (solo ADMIN)
        └── auditoria.js      Historial de auditoría, solo lectura (solo ADMIN)
```

## Cómo ejecutarlo

Es una app 100% estática, pero usa **ES Modules** (`<script type="module">`), así que
el navegador no la puede abrir con doble clic (`file://`); debe servirse por HTTP.

Además, el backend (`CorsConfig.java`) sólo acepta peticiones **CORS** desde:

- `http://localhost:3000`
- `http://localhost:5173`

Por eso debes servir esta carpeta exactamente en uno de esos dos puertos. La forma más
simple:

```bash
cd logitrack
python3 -m http.server 5173
# o, si tienes Node:
npx serve -l 5173
```

Luego abre `http://localhost:5173` en el navegador.

> Si prefieres otro puerto u origen, agrégalo a la lista `setAllowedOrigins(...)` en
> `CorsConfig.java` del backend y recompílalo.

## Backend

1. Levanta MySQL y crea la base con `JP/MySQL/Schema.sql` y `JP/MySQL/Data.sql`.
2. Ejecuta el backend Spring Boot (por defecto queda en `http://localhost:8080`).
3. En la pantalla de login del frontend hay un campo **"URL de la API"** (por defecto
   `http://localhost:8080/api`); solo cámbialo si tu backend corre en otro host o puerto.
   El valor se guarda en `localStorage` para la próxima vez.

### Usuario administrador inicial

El backend crea el primer usuario `ADMIN` automáticamente al arrancar la API (con la
contraseña ya cifrada correctamente), así que puedes iniciar sesión con esas
credenciales desde el primer arranque sin tocar la base de datos a mano.

## Cómo funciona la autenticación

- El login llama a `POST /api/auth/login` y recibe `{ token, usuarioId, nombre, email }`.
- El **rol** no viene en esa respuesta: el frontend decodifica el payload del JWT
  (`js/state.js`) para leer el claim `rol` que el backend sí incluye al firmar el token.
- El token se guarda en `localStorage` y se envía como `Authorization: Bearer <token>`
  en cada petición (`js/api.js`).
- Si el rol es `EMPLEADO`, las secciones **Usuarios** y **Auditoría** quedan ocultas en
  el menú y no son accesibles por URL (coincide con `SecurityConfig.java`, que exige
  `hasRole("ADMIN")` para esos endpoints).
- Un `401` en cualquier petición cierra la sesión automáticamente y vuelve al login.

## Reglas de negocio respetadas en el formulario de movimientos

- **Entrada**: sólo pide bodega destino.
- **Salida**: sólo pide bodega origen.
- **Transferencia**: pide origen y destino (deben ser distintos).
- Los movimientos **no se pueden editar ni eliminar** una vez creados (el backend lo
  rechaza explícitamente), por eso esa vista sólo ofrece crear y ver el detalle.

## Personalización visual

Toda la paleta, tipografía y espaciados viven como variables CSS en `:root` al inicio de
`css/styles.css` — cambia los valores ahí para ajustar el tema sin tocar el resto del
código.
