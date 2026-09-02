# LogiTrack — Sistema de Gestión de Inventario

API REST desarrollada con **Spring Boot** para la administración de bodegas, productos, inventarios, movimientos de mercancía, usuarios y auditoría de operaciones de la empresa **LogiTrack S.A.**

El proyecto incluye:

- Backend REST con Spring Boot.
- Persistencia con Spring Data JPA y MySQL.
- Autenticación y autorización mediante **JWT**.
- Control de acceso por roles: `ADMIN` y `EMPLEADO`.
- Documentación interactiva con **Swagger / OpenAPI**.
- Validación de datos con Jakarta Validation.
- Manejo global de excepciones.
- Auditoría mediante triggers de MySQL.
- Frontend estático desarrollado con **HTML, CSS y JavaScript**, consumiendo la API mediante `fetch`.
- Gestión de entradas, salidas y transferencias de inventario.

---

## 📋 Tabla de contenidos

- [Descripción](#-descripción)
- [Tecnologías](#-tecnologías)
- [Funcionalidades](#-funcionalidades)
- [Arquitectura del proyecto](#-arquitectura-del-proyecto)
- [Requisitos](#-requisitos)
- [Instalación](#-instalación)
- [Configuración de la base de datos](#-configuración-de-la-base-de-datos)
- [Configuración del backend](#-configuración-del-backend)
- [Ejecución](#-ejecución)
- [Swagger / OpenAPI](#-swagger--openapi)
- [Autenticación JWT](#-autenticación-jwt)
- [Roles y permisos](#-roles-y-permisos)
- [Endpoints](#-endpoints)
- [Ejemplos de peticiones](#-ejemplos-de-peticiones)
- [Frontend](#-frontend)
- [Capturas de Swagger y pruebas](#-capturas-de-swagger-y-pruebas)
- [Pruebas](#-pruebas)
- [Solución de problemas](#-solución-de-problemas)
- [Autor](#-autor)

---

## 📦 Descripción

**LogiTrack** es un sistema para controlar el inventario de una empresa que administra varias bodegas ubicadas en diferentes ciudades.

La aplicación permite:

- Registrar y administrar bodegas.
- Registrar y administrar productos.
- Asociar productos a bodegas mediante registros de inventario.
- Consultar y actualizar existencias.
- Registrar entradas de mercancía.
- Registrar salidas de mercancía.
- Realizar transferencias entre bodegas.
- Administrar usuarios del sistema.
- Consultar el historial de auditoría.
- Proteger los recursos mediante autenticación JWT.
- Aplicar permisos dependiendo del rol del usuario.

---

## 🛠️ Tecnologías

| Tecnología | Uso |
|---|---|
| Java 17 | Lenguaje de programación |
| Spring Boot 4.1.0 | Framework principal |
| Spring Web MVC | Desarrollo de API REST |
| Spring Data JPA | Persistencia y acceso a datos |
| Spring Security | Seguridad y autorización |
| JWT / JJWT 0.12.6 | Autenticación mediante tokens |
| MySQL | Base de datos |
| Lombok | Reducción de código repetitivo |
| Jakarta Validation | Validación de solicitudes |
| SpringDoc OpenAPI 3.0.1 | Swagger / documentación de la API |
| Maven | Gestión y construcción del proyecto |
| HTML5 | Frontend |
| CSS3 | Estilos del frontend |
| JavaScript ES Modules | Lógica y consumo de la API |

---

## ✨ Funcionalidades

### Usuarios

- Crear usuarios.
- Consultar usuarios.
- Actualizar usuarios.
- Eliminar usuarios.
- Inicio de sesión.
- Autenticación mediante JWT.
- Autorización por roles.

### Bodegas

- Crear bodegas.
- Consultar una bodega.
- Listar bodegas.
- Actualizar bodegas.
- Eliminar bodegas.

### Productos

- Crear productos.
- Consultar un producto.
- Listar productos.
- Actualizar productos.
- Eliminar productos.

### Inventario

- Crear registros de inventario.
- Consultar inventario.
- Listar inventarios.
- Actualizar stock.
- Eliminar registros de inventario.

### Movimientos

- Registrar entradas.
- Registrar salidas.
- Registrar transferencias entre bodegas.
- Consultar movimientos.
- Consultar el detalle de un movimiento.

### Auditoría

- Consultar registros de auditoría.
- Registrar automáticamente operaciones `INSERT`, `UPDATE` y `DELETE` mediante triggers de MySQL.
- Acceso exclusivo para usuarios con rol `ADMIN`.

---

## 📁 Arquitectura del proyecto

```text
SpringBoot_Proyect_JP-main/
│
├── MySQL/
│   ├── Schema.sql
│   └── Data.sql
│
├── Proyecto_SpringBoot_JP-main/
│   └── JP/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/
│       │   │   │   └── com/ProyectoSpringBoot/JP/
│       │   │   │       ├── Auth/
│       │   │   │       ├── Mapper/
│       │   │   │       ├── Model/
│       │   │   │       ├── Repository/
│       │   │   │       ├── Service/
│       │   │   │       ├── config/
│       │   │   │       ├── controller/
│       │   │   │       ├── dto/
│       │   │   │       └── exception/
│       │   │   │
│       │   │   └── resources/
│       │   │       └── application.properties
│       │   │
│       │   └── test/
│       │
│       ├── pom.xml
│       ├── mvnw
│       └── mvnw.cmd
│
└── Frontend/
    └── logitrack/
        ├── index.html
        ├── css/
        │   └── styles.css
        └── js/
            ├── api.js
            ├── config.js
            ├── main.js
            ├── router.js
            ├── state.js
            ├── ui.js
            └── views/
                ├── dashboard.js
                ├── usuarios.js
                ├── bodegas.js
                ├── productos.js
                ├── inventario.js
                ├── movimientos.js
                └── auditoria.js
```

### Capas del backend

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
JPA / Hibernate
    ↓
MySQL
```

Además:

```text
Request DTO → Service → Mapper → Entity
Entity → Mapper → Response DTO
```

---

## 💻 Requisitos

Antes de ejecutar el proyecto se necesita tener instalado:

1. **JDK 17**
2. **MySQL 8.x** o una versión compatible.
3. **Maven** (opcional, ya que el proyecto incluye Maven Wrapper).
4. Un IDE como IntelliJ IDEA, Eclipse, NetBeans o VS Code.
5. Un navegador web.
6. Para ejecutar el frontend, un servidor HTTP local.

---

# 🗄️ Configuración de la base de datos

## 1. Crear la base de datos

Dentro del proyecto se encuentran los archivos:

```text
MySQL/Schema.sql
MySQL/Data.sql
```

Primero ejecutar:

```sql
SOURCE MySQL/Schema.sql;
```

Después:

```sql
SOURCE MySQL/Data.sql;
```

También pueden abrirse directamente desde **MySQL Workbench**, **DBeaver** u otra herramienta compatible.

El script crea la base:

```text
logitrack_db
```

### Estructura principal

```text
usuario
bodega
producto
inventario
movimiento
movimiento_detalle
auditoria
```

---

## 2. Configurar las credenciales de MySQL

El backend utiliza actualmente la siguiente configuración en:

```text
Proyecto_SpringBoot_JP-main/JP/src/main/resources/application.properties
```

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/logitrack_db
spring.datasource.username=campus2023
spring.datasource.password=campus2023
```

Si las credenciales de MySQL son diferentes en tu equipo, modifica:

```properties
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
```

La aplicación utiliza:

```properties
spring.jpa.hibernate.ddl-auto=none
```

Por lo tanto, **las tablas deben existir previamente en MySQL** mediante los scripts SQL.

---

# ⚙️ Configuración del backend

Ubicarse en:

```text
Proyecto_SpringBoot_JP-main/JP/
```

### Con Maven Wrapper — Windows

```bash
.\mvnw.cmd clean install
```

Ejecutar:

```bash
.\mvnw.cmd spring-boot:run
```

### Con Maven instalado

```bash
mvn clean install
```

Ejecutar:

```bash
mvn spring-boot:run
```

### Desde el IDE

Abrir el proyecto Maven y ejecutar:

```text
src/main/java/com/ProyectoSpringBoot/JP/JpApplication.java
```

La API quedará disponible normalmente en:

```text
http://localhost:8080
```

---

# 🔐 Usuario administrador inicial

Al iniciar el backend, si la tabla `usuario` está vacía, `DataInitializer` crea automáticamente un usuario administrador.

### Credenciales iniciales

```text
Email: admin@logitrack.com
Contraseña: Admin123
Rol: ADMIN
```

> Por seguridad, estas credenciales deben cambiarse en un entorno real.

---

# 📖 Swagger / OpenAPI

La API cuenta con documentación interactiva mediante Swagger.

Una vez iniciado Spring Boot, ingresar a:

```text
http://localhost:8080/swagger-ui/index.html
```

También está disponible la especificación OpenAPI en:

```text
http://localhost:8080/v3/api-docs
```

Swagger permite:

- Consultar los endpoints.
- Ver los modelos de petición y respuesta.
- Ejecutar pruebas directamente desde el navegador.
- Autenticarse mediante JWT.
- Probar operaciones CRUD.
- Revisar códigos de respuesta.

---

# 🔑 Autenticación JWT

Los endpoints protegidos requieren un token JWT.

## 1. Iniciar sesión

Realizar:

```http
POST /api/auth/login
```

Body:

```json
{
  "email": "admin@logitrack.com",
  "password": "Admin123"
}
```

Respuesta aproximada:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "usuarioId": 1,
  "nombre": "Administrador",
  "email": "admin@logitrack.com"
}
```

## 2. Autorizar Swagger

Copiar únicamente el token recibido y seleccionar el botón:

```text
Authorize
```

Introducir:

```text
Bearer TU_TOKEN
```

o, dependiendo de la interfaz de Swagger:

```text
TU_TOKEN
```

Swagger enviará posteriormente:

```http
Authorization: Bearer TU_TOKEN
```

en las peticiones protegidas.

---

# 👥 Roles y permisos

El sistema maneja dos roles:

| Rol | Permisos |
|---|---|
| `ADMIN` | Usuarios, auditoría, bodegas, productos, inventario y movimientos |
| `EMPLEADO` | Bodegas, productos, inventario y movimientos |

### Acceso exclusivo de ADMIN

```text
/api/usuarios/**
/api/auditorias/**
```

### Acceso para ADMIN y EMPLEADO

```text
/api/bodegas/**
/api/productos/**
/api/inventarios/**
/api/movimientos/**
```

El login:

```text
/api/auth/**
```

y la documentación Swagger son públicos.

---

# 🔌 Endpoints

## 🔐 Autenticación

| Método | Endpoint | Descripción | Autenticación |
|---|---|---|---|
| POST | `/api/auth/login` | Iniciar sesión y obtener JWT | No |

---

## 👤 Usuarios

| Método | Endpoint | Descripción | Rol |
|---|---|---|---|
| POST | `/api/usuarios` | Crear usuario | ADMIN |
| GET | `/api/usuarios` | Listar usuarios | ADMIN |
| GET | `/api/usuarios/{id}` | Obtener usuario | ADMIN |
| PUT | `/api/usuarios/{id}` | Actualizar usuario | ADMIN |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario | ADMIN |

---

## 🏭 Bodegas

| Método | Endpoint | Descripción | Rol |
|---|---|---|---|
| POST | `/api/bodegas` | Crear bodega | ADMIN / EMPLEADO |
| GET | `/api/bodegas` | Listar bodegas | ADMIN / EMPLEADO |
| GET | `/api/bodegas/{id}` | Obtener bodega | ADMIN / EMPLEADO |
| PUT | `/api/bodegas/{id}` | Actualizar bodega | ADMIN / EMPLEADO |
| DELETE | `/api/bodegas/{id}` | Eliminar bodega | ADMIN / EMPLEADO |

---

## 📦 Productos

| Método | Endpoint | Descripción | Rol |
|---|---|---|---|
| POST | `/api/productos` | Crear producto | ADMIN / EMPLEADO |
| GET | `/api/productos` | Listar productos | ADMIN / EMPLEADO |
| GET | `/api/productos/{id}` | Obtener producto | ADMIN / EMPLEADO |
| PUT | `/api/productos/{id}` | Actualizar producto | ADMIN / EMPLEADO |
| DELETE | `/api/productos/{id}` | Eliminar producto | ADMIN / EMPLEADO |

---

## 📊 Inventarios

| Método | Endpoint | Descripción | Rol |
|---|---|---|---|
| POST | `/api/inventarios` | Crear inventario | ADMIN / EMPLEADO |
| GET | `/api/inventarios` | Listar inventarios | ADMIN / EMPLEADO |
| GET | `/api/inventarios/{id}` | Obtener inventario | ADMIN / EMPLEADO |
| PUT | `/api/inventarios/{id}` | Actualizar inventario | ADMIN / EMPLEADO |
| DELETE | `/api/inventarios/{id}` | Eliminar inventario | ADMIN / EMPLEADO |

---

## 🚚 Movimientos

| Método | Endpoint | Descripción | Rol |
|---|---|---|---|
| POST | `/api/movimientos` | Registrar movimiento | ADMIN / EMPLEADO |
| GET | `/api/movimientos` | Listar movimientos | ADMIN / EMPLEADO |
| GET | `/api/movimientos/{id}` | Obtener movimiento | ADMIN / EMPLEADO |

Los movimientos soportan:

```text
ENTRADA
SALIDA
TRANSFERENCIA
```

### Reglas

**ENTRADA**

- Requiere `bodegaDestinoId`.
- Aumenta el stock.

**SALIDA**

- Requiere `bodegaOrigenId`.
- Disminuye el stock.
- No permite retirar una cantidad superior al stock disponible.

**TRANSFERENCIA**

- Requiere `bodegaOrigenId`.
- Requiere `bodegaDestinoId`.
- Las bodegas deben ser diferentes.
- Disminuye el stock de origen y aumenta el stock de destino.

---

## 📝 Auditoría

| Método | Endpoint | Descripción | Rol |
|---|---|---|---|
| GET | `/api/auditorias` | Listar auditorías | ADMIN |
| GET | `/api/auditorias/{id}` | Consultar auditoría | ADMIN |

La auditoría registra operaciones:

```text
INSERT
UPDATE
DELETE
```

sobre las entidades configuradas en la base de datos.

---

# 🧪 Ejemplos de peticiones

## Crear una bodega

```http
POST /api/bodegas
Authorization: Bearer TU_TOKEN
Content-Type: application/json
```

```json
{
  "nombre": "Bodega Occidente",
  "ubicacion": "Piedecuesta",
  "capacidad": 800,
  "encargado": "Juan Pérez"
}
```

---

## Crear un producto

```http
POST /api/productos
Authorization: Bearer TU_TOKEN
Content-Type: application/json
```

```json
{
  "nombre": "Laptop HP 15",
  "categoria": "Computadores",
  "precio": 2500000
}
```

---

## Crear inventario

```http
POST /api/inventarios
Authorization: Bearer TU_TOKEN
Content-Type: application/json
```

```json
{
  "bodegaId": 1,
  "productoId": 1,
  "stock": 20
}
```

---

## Registrar una entrada

```http
POST /api/movimientos
Authorization: Bearer TU_TOKEN
Content-Type: application/json
```

```json
{
  "tipo": "ENTRADA",
  "bodegaDestinoId": 1,
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 10
    }
  ]
}
```

---

## Registrar una salida

```http
POST /api/movimientos
Authorization: Bearer TU_TOKEN
Content-Type: application/json
```

```json
{
  "tipo": "SALIDA",
  "bodegaOrigenId": 1,
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2
    }
  ]
}
```

---

## Registrar una transferencia

```http
POST /api/movimientos
Authorization: Bearer TU_TOKEN
Content-Type: application/json
```

```json
{
  "tipo": "TRANSFERENCIA",
  "bodegaOrigenId": 1,
  "bodegaDestinoId": 2,
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 5
    }
  ]
}
```

> Los nombres exactos de los campos deben coincidir con los DTO `MovimientoRequest` y `MovimientoDetalleRequest` incluidos en el proyecto.

---

# 🖥️ Frontend

El proyecto incluye un frontend estático ubicado en:

```text
Frontend/logitrack/
```

Está desarrollado utilizando:

- HTML5
- CSS3
- JavaScript
- ES Modules
- Fetch API
- LocalStorage

No utiliza frameworks ni requiere un proceso de compilación.

## Estructura

```text
Frontend/logitrack/
│
├── index.html
├── css/
│   └── styles.css
│
└── js/
    ├── api.js
    ├── config.js
    ├── main.js
    ├── router.js
    ├── state.js
    ├── ui.js
    └── views/
        ├── dashboard.js
        ├── usuarios.js
        ├── bodegas.js
        ├── productos.js
        ├── inventario.js
        ├── movimientos.js
        └── auditoria.js
```

## ¿Qué consume el frontend?

El frontend consume directamente los endpoints del backend:

```text
/api/auth
/api/usuarios
/api/bodegas
/api/productos
/api/inventarios
/api/movimientos
/api/auditorias
```

El cliente JavaScript utiliza `fetch` y agrega automáticamente el JWT:

```http
Authorization: Bearer TU_TOKEN
```

cuando existe una sesión iniciada.

---

# ▶️ Ejecutar el frontend

Debido a que el frontend utiliza **ES Modules**, no se recomienda abrir `index.html` directamente con `file://`.

Debe ejecutarse mediante un servidor HTTP local.

## Opción 1 — Python

Abrir una terminal en:

```text
Frontend/logitrack/
```

Ejecutar:

```bash
python -m http.server 5173
```

Luego ingresar a:

```text
http://localhost:5173
```

## Opción 2 — Node.js

Desde la carpeta del frontend:

```bash
npx serve -l 5173
```

Luego ingresar a:

```text
http://localhost:5173
```

El backend debe estar ejecutándose simultáneamente en:

```text
http://localhost:8080
```

---

# 🌐 Configuración CORS

El backend permite actualmente peticiones provenientes de:

```text
http://localhost:3000
http://localhost:5173
```

Esta configuración se encuentra en:

```text
src/main/java/com/ProyectoSpringBoot/JP/config/CorsConfig.java
```

Si el frontend se ejecuta desde otro origen, es necesario agregarlo en `setAllowedOrigins()`.

---

# 📸 Capturas de Swagger y pruebas

Para documentar la ejecución del proyecto, se recomienda agregar las capturas dentro de:

```text
docs/
└── screenshots/
    ├── swagger-login.png
    ├── swagger-authorize.png
    ├── swagger-bodegas.png
    ├── swagger-productos.png
    ├── swagger-inventario.png
    ├── swagger-movimientos.png
    ├── swagger-usuarios.png
    ├── swagger-auditoria.png
    ├── prueba-login.png
    ├── prueba-crud-bodega.png
    └── prueba-movimiento.png
```

Después pueden incorporarse al README con:

```markdown
## Swagger

![Swagger - Login](docs/screenshots/swagger-login.png)

![Swagger - Autorización JWT](docs/screenshots/swagger-authorize.png)

## Pruebas de endpoints

![Prueba CRUD Bodega](docs/screenshots/prueba-crud-bodega.png)

![Prueba de movimiento](docs/screenshots/prueba-movimiento.png)
```

### Capturas mínimas recomendadas

Para evidenciar el funcionamiento del proyecto:

- Swagger mostrando todos los grupos de endpoints.
- Login exitoso y generación del JWT.
- Autorización mediante `Bearer Token`.
- Consulta de bodegas.
- CRUD de productos.
- Consulta/actualización de inventario.
- Registro de una entrada.
- Registro de una salida.
- Registro de una transferencia.
- Consulta de auditoría con usuario `ADMIN`.
- Frontend funcionando en el navegador.

> Las imágenes deben ser capturas reales de la ejecución del proyecto. No se incluyen capturas ficticias en este README.

---

# 🧪 Pruebas

El proyecto incluye pruebas en:

```text
src/test/java/com/ProyectoSpringBoot/JP/
```

Para ejecutar las pruebas:

### Windows

```bash
.\mvnw.cmd test
```

### Maven

```bash
mvn test
```

Para verificar el proyecto completo:

```bash
.\mvnw.cmd clean verify
```

---

# 🔄 Flujo recomendado de prueba

Para probar el sistema desde cero:

```text
1. Crear base de datos
        ↓
2. Ejecutar Schema.sql
        ↓
3. Ejecutar Data.sql
        ↓
4. Configurar credenciales MySQL
        ↓
5. Ejecutar Spring Boot
        ↓
6. Abrir Swagger
        ↓
7. Login con admin@logitrack.com
        ↓
8. Copiar JWT
        ↓
9. Authorize en Swagger
        ↓
10. Probar bodegas/productos
        ↓
11. Crear o consultar inventario
        ↓
12. Registrar movimientos
        ↓
13. Consultar auditoría
        ↓
14. Ejecutar frontend
```

---

# 🧯 Solución de problemas

## Error de conexión con MySQL

Verificar:

```text
- MySQL está ejecutándose.
- La base logitrack_db existe.
- Usuario y contraseña son correctos.
- El puerto es 3306.
```

## Error 401 Unauthorized

Significa que la petición no tiene un JWT válido.

Solución:

1. Ejecutar `/api/auth/login`.
2. Copiar el token.
3. Abrir `Authorize` en Swagger.
4. Introducir el token.
5. Repetir la petición.

## Error 403 Forbidden

El usuario está autenticado, pero su rol no tiene permiso para acceder al endpoint.

Por ejemplo:

```text
/api/usuarios/**
/api/auditorias/**
```

requieren:

```text
ADMIN
```

## Error CORS desde el frontend

Verificar que el frontend esté ejecutándose en:

```text
http://localhost:3000
```

o:

```text
http://localhost:5173
```

y que Spring Boot esté ejecutándose en:

```text
http://localhost:8080
```

---

# 🔒 Seguridad

El proyecto implementa:

- Contraseñas almacenadas mediante BCrypt.
- Autenticación stateless.
- Tokens JWT.
- Filtro de autenticación JWT.
- Autorización por roles.
- Protección de endpoints.
- CORS configurado.
- Validación de datos recibidos.
- Manejo global de excepciones.

La clave JWT se encuentra actualmente en `application.properties` para fines de desarrollo. En un entorno real debe utilizarse una variable de entorno o un mecanismo seguro de gestión de secretos.

---

# 📌 Consideraciones

Este proyecto está configurado principalmente para un entorno de desarrollo local.

Antes de llevarlo a producción se recomienda:

- Cambiar las credenciales iniciales.
- No almacenar secretos directamente en `application.properties`.
- Utilizar variables de entorno.
- Configurar HTTPS.
- Utilizar una clave JWT segura y almacenada fuera del repositorio.
- Configurar CORS únicamente para los dominios autorizados.
- Añadir pruebas automatizadas adicionales.
- Configurar perfiles de Spring para desarrollo y producción.

---

# 👨‍💻 Autor

**Juan Pablo Beltrán Jiménez**

Proyecto académico de desarrollo backend con Spring Boot.

---

## 📄 Licencia

Proyecto desarrollado con fines académicos y educativos.
