# SGS Market — Resumen del Proyecto

**Proyecto universitario** — Universidad del Quindío  
**Autor:** Sebastián Agudelo  
**Estado actual:** Producción activa en https://sgsmarket.duckdns.org

---

## Descripción

SGS Market es una aplicación web de supermercado colombiano con catálogo de productos, carrito de compras, pagos con MercadoPago, panel de administración y monitoreo en tiempo real.

---

## Arquitectura

```
┌─────────────────────────────────────────────────────┐
│                  GCP Compute Engine                  │
│           (e2-small, us-central1-c, 20GB)            │
│                                                     │
│  nginx (80/443) ──► Angular build (static)          │
│                 ──► Spring Boot :8080 (API)          │
│                 ──► Jenkins :8080/jenkins (CI/CD)    │
│                 ──► Grafana :3000/grafana (monitor)  │
│                                                     │
│  Docker (host network):                             │
│    prometheus :9090  ──scrape──► /actuator/prometheus│
│    grafana    :3000                                  │
└─────────────────────────────────────────────────────┘
```

**DNS:** sgsmarket.duckdns.org (DuckDNS, IP estática GCP, cron refresh cada 5 min)

---

## Stack tecnológico

### Backend — `sgs-backend/`
| Tecnología | Uso |
|---|---|
| Spring Boot 3 | Framework principal |
| Spring Security + JWT | Autenticación / autorización |
| JPA / Hibernate | ORM |
| MySQL | Base de datos |
| Lombok | Boilerplate reduction |
| Spring Actuator + Micrometer | Métricas `/actuator/prometheus` |
| BCrypt | Hash de contraseñas |

### Frontend — `sgs-frontend/`
| Tecnología | Uso |
|---|---|
| Angular 20 (standalone) | Framework SPA |
| TypeScript | Lenguaje |
| Control flow `@if / @for` | Sintaxis moderna Angular |
| Lazy loading | Rutas cargadas bajo demanda |
| Cypress 13 | Tests E2E |
| cypress-testrail-simple | Reporte automático a TestRail |

### Infraestructura
| Herramienta | Uso |
|---|---|
| Jenkins | CI/CD pipeline (auto-trigger en push) |
| nginx | Reverse proxy + HTTPS |
| Prometheus | Scraping de métricas |
| Grafana | Dashboard de monitoreo |
| Docker Compose | Stack de monitoreo |
| systemd | Servicio del backend (`sgs-backend.service`) |

---

## Páginas y funcionalidades

### Rutas públicas
- `/` — **Home** con hero, beneficios, carruseles por categoría, grid de categorías
- `/login` — Login con JWT
- `/registro` — Registro de usuario
- `/verificar` — Verificación de cuenta

### Rutas de usuario autenticado
- `/productos` — Catálogo completo con filtro por categoría
- `/productos/:id` — Detalle de producto
- `/carrito` — Carrito con cálculo de total y checkout
- `/mis-pedidos` — Historial de pedidos
- `/perfil` — Datos del usuario

### Rutas de administrador
- `/admin/clientes` — CRUD completo de clientes (crear, editar, estado, contraseña, direcciones)
- `/admin/pedidos` — Gestión de pedidos
- `/admin/inventario-alertas` — Alertas de stock bajo

---

## CI/CD — Jenkinsfile

Pipeline de 5 etapas ejecutado en la misma VM de producción:

```
1. Backend: Build      → mvnw clean package -DskipTests
2. Backend: Unit Tests → mvnw test (excluye SgsBackendApplicationTests)
3. Frontend: Build     → npm ci + npm run build
4. Deploy              → deploy/deploy-jenkins.sh (systemctl restart + nginx reload)
5. E2E: Cypress        → npm run cy:run (después de 30s sleep para backend)
```

El deploy ocurre **antes** de los E2E para que Cypress pruebe la versión recién desplegada.

---

## Tests E2E — Cypress

12 tests distribuidos en 4 archivos, mapeados a casos TestRail C46–C57:

| Archivo | Casos | Descripción |
|---|---|---|
| `auth.cy.ts` | C46–C48 | Login exitoso, login fallido, cerrar sesión |
| `catalog.cy.ts` | C49–C51 | Lista productos, detalle, filtro por categoría |
| `cart.cy.ts` | C52–C54 | Agregar al carrito, ver carrito, botón checkout |
| `admin.cy.ts` | C55–C57 | Acceso admin, tabla clientes, alertas inventario |

### Integración TestRail
- **Paquete:** `cypress-testrail-simple` (reemplaza el abandonado `cypress-testrail-reporter`)
- **Configuración:** plugin registrado via `require()` en `cypress.config.ts` (evita interop TS/CommonJS)
- **Credenciales en Jenkins:** `testrail-host`, `testrail-user`, `testrail-api-key`, `testrail-project-id`, `testrail-suite-id`
- **Resultado:** cada corrida de CI crea un Test Run en https://sgsmarket.testrail.io con estado pass/fail por caso

---

## Monitoreo — Grafana

- **URL:** https://sgsmarket.duckdns.org/grafana
- **Datasource:** Prometheus en `http://127.0.0.1:9090` (host network)
- **Métricas disponibles:** HTTP requests, JVM memory, GC, threads, DB pool (Actuator + Micrometer)
- **Configuración:** `deploy/docker-compose.monitoring.yml`

---

## Base de datos

- 14 categorías: Lacteos, Granos, Frutas, Panadería, Bebidas, Carnes, Verduras, Snacks, Aseo, Enlatados, Congelados, Dulces, Mascotas, Bebes
- ~90 productos con imágenes via loremflickr.com
- Scripts en `deploy/`: `seed-data.sql`, `seed-data-extended.sql`, `seed-completar.sql`, `fix-categorias.sql`

---

## Pagos

- Integración con **MercadoPago** en flujo de checkout
- Credenciales configuradas como variables de entorno en el servidor

---

## Estructura de archivos clave

```
sgs-project/
├── sgs-backend/
│   └── src/main/java/com/uniquindio/backend/
│       ├── controller/   (AdminController, ProductoController, PedidoController…)
│       ├── service/      (AdminService, ProductoService…)
│       ├── model/        (Usuario, Producto, Pedido, Carrito…)
│       ├── dto/          (request/response DTOs)
│       ├── security/     (JWT filter, config)
│       └── repository/   (Spring Data JPA repos)
├── sgs-frontend/
│   └── src/app/
│       ├── pages/        (home, login, carrito, admin-clientes…)
│       ├── components/   (navbar, footer…)
│       └── services/     (auth, producto, carrito…)
│   └── cypress/
│       ├── e2e/          (auth, catalog, cart, admin tests)
│       └── support/      (commands: loginAsAdmin, loginAsTestUser)
└── deploy/
    ├── deploy-jenkins.sh
    ├── docker-compose.monitoring.yml
    ├── monitoring/       (prometheus.yml, grafana provisioning)
    ├── nginx/            (site config)
    └── *.sql             (seed scripts)
```

---

## Estado actual (mayo 2026)

- [x] Backend REST API completo con autenticación JWT
- [x] Frontend Angular con todas las páginas funcionales
- [x] Admin CRUD de clientes completo (incluye direcciones, estado, reset password)
- [x] CI/CD con Jenkins (auto-deploy en push a main)
- [x] Monitoreo con Prometheus + Grafana en producción
- [x] 12 tests E2E Cypress pasando en CI
- [x] Integración TestRail configurada (cypress-testrail-simple, casos C46–C57)
- [x] Home page diferenciada con carruseles y grid de categorías
- [x] DNS estable con IP estática GCP + DuckDNS
- [ ] Verificar primer Test Run exitoso en TestRail (pendiente resultado del último push)

---

## Posibles mejoras futuras

### Funcionalidad
- **Búsqueda avanzada** — autocompletado, búsqueda por descripción, filtros por precio/stock
- **Notificaciones** — email al crear pedido, SMS/WhatsApp de estado de despacho
- **Wishlist / favoritos** — guardar productos para después
- **Cupones de descuento** — sistema de códigos promocionales

### Técnico
- **PWA** — Service Worker para funcionar offline y agregar a pantalla inicio
- **Caché con Redis** — cachear catálogo de productos, reducir carga a MySQL
- **WebSockets** — actualizaciones de pedido en tiempo real sin polling
- **Paginación real en backend** — actualmente el frontend filtra en memoria; con muchos productos escalaría mal
- **Tests unitarios en Angular** — actualmente saltados en CI por incompatibilidad Karma/Chromium headless en Jenkins
- **Docker para backend** — actualmente corre como systemd service; dockerizarlo simplifica el deploy
- **Kubernetes o Cloud Run** — para escalar horizontalmente si aumenta la carga
- **Rotación automática de secrets** — JWT secret y DB password hardcoded en env; usar Secret Manager de GCP
- **CDN para imágenes** — actualmente loremflickr como placeholder; con imágenes reales usar GCS + CDN
- **Rate limiting** — protección contra abuso en endpoints de login y pago
- **Alertas en Grafana** — enviar notificación a Slack/email cuando métricas superen umbrales
