# SGS Market — Frontend

> Aplicacion web desarrollada con **Angular 20** para un supermercado en linea. Incluye catalogo de productos, carrito de compras, pagos con MercadoPago, seguimiento de pedidos, perfil de usuario con direcciones, y panel completo de administracion.

**Produccion:** [https://sgsmarket.duckdns.org](https://sgsmarket.duckdns.org)

---

## Tabla de Contenidos

- [Caracteristicas](#caracteristicas)
- [Tecnologias](#tecnologias)
- [Requisitos Previos](#requisitos-previos)
- [Instalacion y Ejecucion](#instalacion-y-ejecucion)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Vistas y Rutas](#vistas-y-rutas)
- [Autenticacion](#autenticacion)
- [Variables de Entorno](#variables-de-entorno)
- [Build para Produccion](#build-para-produccion)
- [Despliegue](#despliegue)

---

## Caracteristicas

### Publico
- Catalogo de productos con busqueda en tiempo real y filtro por categorias
- Barra de categorias en el navbar para navegacion rapida
- Detalle de producto con galeria de imagenes, stock y precio en COP
- Registro con validacion de cedula y telefono, verificacion por correo

### Cliente (autenticado)
- Carrito de compras con cantidades editables y resumen de total
- Pago integrado con MercadoPago Checkout Pro
- Historial de pedidos con estados en tiempo real (7 estados)
- Factura descargable por pedido
- Perfil editable con gestion de direcciones (CRUD + predeterminada)

### Administrador
- CRUD de productos con activacion/desactivacion y eliminacion definitiva
- CRUD de categorias con modal integrado
- Panel de pedidos con filtro por estado y cambio de estado
- Panel de clientes con busqueda, filtros y estadisticas (total pedidos, total gastado)

### Tecnico
- Componentes standalone con lazy loading
- Reactive Forms con validaciones
- Interceptor HTTP para JWT automatico
- Guards: `authGuard`, `adminGuard`, `guestGuard`
- Navbar responsive con menu segun rol
- Iconos SVG (sin emojis)

---

## Tecnologias

| Tecnologia | Version |
|---|---|
| Angular | 20.2.0 |
| TypeScript | 5.9.x |
| Node.js | 22.x |
| npm | 11.x |
| Angular Router | Lazy Loading |
| Angular Forms | Reactive Forms |
| HttpClient | Con interceptor funcional |

---

## Requisitos Previos

- **Node.js 18+** (recomendado 22.x)
- **npm 9+**
- **Angular CLI 20+**
  ```bash
  npm install -g @angular/cli
  ```
- El **backend SGS** corriendo en `https://localhost:8443`

---

## Instalacion y Ejecucion

```bash
# Clonar el repositorio
git clone <url-del-repo>
cd sgs-frontend

# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
ng serve
```

La aplicacion estara disponible en **`http://localhost:4200`**.

> El proxy de desarrollo (`proxy.conf.json`) redirige `/api` y `/uploads` a `https://localhost:8443`.

---

## Estructura del Proyecto

```
src/
|-- environments/
|   |-- environment.ts              # Desarrollo
|   +-- environment.prod.ts         # Produccion (apiUrl: /api)
+-- app/
    |-- components/
    |   +-- navbar/                  # Navbar responsive con menu segun rol
    |-- guards/
    |   +-- auth.guard.ts           # authGuard, adminGuard, guestGuard
    |-- interceptors/
    |   +-- auth.interceptor.ts     # Adjunta Bearer token en cada request
    |-- models/
    |   |-- auth.model.ts           # AuthResponse, LoginRequest, RegistroRequest
    |   |-- categoria.model.ts
    |   |-- cliente.model.ts        # ClienteResponse (panel admin)
    |   |-- pedido.model.ts         # PedidoResponse, DetallePedidoResponse, etc.
    |   |-- perfil.model.ts         # PerfilResponse, DireccionRequest/Response
    |   +-- producto.model.ts
    |-- pages/
    |   |-- admin-clientes/         # Panel de gestion de clientes
    |   |-- admin-pedidos/          # Panel de gestion de pedidos
    |   |-- carrito/                # Carrito de compras
    |   |-- categorias/             # CRUD de categorias (admin)
    |   |-- login/                  # Inicio de sesion
    |   |-- mis-pedidos/            # Historial de pedidos del cliente
    |   |-- perfil/                 # Perfil y direcciones del usuario
    |   |-- producto-detalle/       # Vista detallada de producto
    |   |-- producto-form/          # Crear/editar producto (admin)
    |   |-- producto-lista/         # Catalogo con busqueda y filtros
    |   |-- registro/               # Registro con cedula y telefono
    |   +-- verificar/              # Verificacion de cuenta por codigo
    |-- services/
    |   |-- admin.service.ts        # Gestion de clientes
    |   |-- auth.service.ts         # Login, logout, JWT, BehaviorSubject
    |   |-- carrito.service.ts      # Carrito local con localStorage
    |   |-- categoria.service.ts    # CRUD categorias
    |   |-- pago.service.ts         # Integracion MercadoPago
    |   |-- pedido.service.ts       # Pedidos y facturas
    |   |-- perfil.service.ts       # Perfil y direcciones
    |   +-- producto.service.ts     # CRUD productos + toggle activo
    |-- app.config.ts               # Providers: HttpClient, Router
    |-- app.routes.ts               # Rutas lazy-loaded con guards
    |-- app.ts                      # Componente raiz
    +-- app.html                    # Layout: navbar + router-outlet
```

---

## Vistas y Rutas

| Ruta | Componente | Guard | Descripcion |
|---|---|---|---|
| `/productos` | ProductoListaComponent | — | Catalogo publico |
| `/productos/:id` | ProductoDetalleComponent | — | Detalle de producto |
| `/productos/nuevo` | ProductoFormComponent | `adminGuard` | Crear producto |
| `/productos/editar/:id` | ProductoFormComponent | `adminGuard` | Editar producto |
| `/categorias` | CategoriasComponent | `adminGuard` | Gestion de categorias |
| `/carrito` | CarritoComponent | `authGuard` | Carrito de compras |
| `/mis-pedidos` | MisPedidosComponent | `authGuard` | Historial de pedidos |
| `/mi-perfil` | PerfilComponent | `authGuard` | Perfil y direcciones |
| `/admin/pedidos` | AdminPedidosComponent | `adminGuard` | Gestion de pedidos |
| `/admin/clientes` | AdminClientesComponent | `adminGuard` | Gestion de clientes |
| `/login` | LoginComponent | `guestGuard` | Inicio de sesion |
| `/registro` | RegistroComponent | `guestGuard` | Crear cuenta |
| `/verificar` | VerificarComponent | — | Verificar con codigo |
| `/` | — | — | Redirige a `/productos` |

---

## Autenticacion

La autenticacion usa **JWT** almacenado en `localStorage`:

1. Al hacer login, el token y los datos del usuario se guardan en `localStorage`
2. El `authInterceptor` agrega `Authorization: Bearer <token>` en cada peticion HTTP
3. Los guards verifican el estado del `AuthService` antes de activar cada ruta

| Rol | Permisos |
|---|---|
| `ADMIN` | Todo: productos, categorias, pedidos, clientes |
| `CLIENTE` | Catalogo, carrito, pedidos propios, perfil |

---

## Variables de Entorno

```typescript
// src/environments/environment.ts (desarrollo)
export const environment = {
  production: false,
  apiUrl: '/api'   // Proxied via proxy.conf.json
};

// src/environments/environment.prod.ts (produccion)
export const environment = {
  production: true,
  apiUrl: '/api'   // Relative, served by Nginx
};
```

### Google Maps API

Para mostrar la dirección en un mapa (componente `app-address-map`) necesitas una clave de Google Maps. Pasos:

1. Ve a https://console.cloud.google.com/ y crea o selecciona un proyecto.
2. Habilita las APIs: **Maps JavaScript API** y **Geocoding API** (o Places si usarás autocompletado).
3. En la sección de credenciales crea una API key y restringe por HTTP (dominios) o por IP según corresponda.
4. Copia la clave en `src/environments/environment.ts` (desarrollo) y `src/environments/environment.prod.ts` (producción) en la propiedad `googleMapsApiKey`.
   - No subas la clave a repositorios públicos.
5. Asegúrate de que el proyecto tenga facturación habilitada (Google Maps requiere facturación activa).

Ejemplo rápido para desarrollo:

```ts
// src/environments/environment.ts
export const environment = {
  production: false,
  apiUrl: '/api',
  googleMapsApiKey: 'TU_CLAVE_AQUI'
}
```

Luego ejecuta `ng serve` y abre la pantalla de `Mi Perfil` > `+ Agregar dirección` para probar el mapa.

---

## Build para Produccion

```bash
ng build --configuration=production
```

Los archivos compilados se generan en `dist/sgs-frontend/browser/`.

---

## Despliegue

En produccion, Nginx sirve los archivos estaticos del frontend y hace proxy al backend:

```
https://sgsmarket.duckdns.org/          -> /var/www/sgs-frontend/ (Angular)
https://sgsmarket.duckdns.org/api/      -> http://localhost:8080  (Spring Boot)
https://sgsmarket.duckdns.org/uploads/  -> /opt/sgs/uploads/      (Imagenes)
```

Para desplegar desde la maquina local:
```bash
bash deploy/deploy.sh <IP_EC2> <PEM_FILE>
```

---

## Proyecto Universitario

Desarrollado para la asignatura **Software 3** — Universidad del Quindio.
Backend: [sgs-backend](../sgs-backend/)
