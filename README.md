<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:0ea5e9,100:6366f1&height=220&section=header&text=SGS%20Market&fontSize=70&fontColor=ffffff&fontAlignY=38&desc=SuperMarket%20Management%20System&descSize=20&descAlignY=60&animation=fadeIn" alt="SGS Market banner" />

### 🛒 Plataforma web full-stack para la gestión integral de un supermercado

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-20-DD0031?style=for-the-badge&logo=angular&logoColor=white)](https://angular.dev/)
[![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)](#)
[![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](#)
[![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](#)
[![Jenkins](https://img.shields.io/badge/Jenkins-CI/CD-D24939?style=for-the-badge&logo=jenkins&logoColor=white)](#)
[![Prometheus](https://img.shields.io/badge/Prometheus-Metrics-E6522C?style=for-the-badge&logo=prometheus&logoColor=white)](#)
[![Grafana](https://img.shields.io/badge/Grafana-Dashboards-F46800?style=for-the-badge&logo=grafana&logoColor=white)](#)
[![Cypress](https://img.shields.io/badge/Cypress-E2E-17202C?style=for-the-badge&logo=cypress&logoColor=white)](#)
[![License](https://img.shields.io/badge/license-MIT-22c55e?style=for-the-badge)](#-licencia)

[![Status](https://img.shields.io/badge/status-en%20producción-success?style=flat-square)](https://sgsmarket.duckdns.org)
[![Deploy](https://img.shields.io/badge/deploy-GCP%20Compute%20Engine-4285F4?style=flat-square&logo=googlecloud&logoColor=white)](#)
[![HTTPS](https://img.shields.io/badge/HTTPS-DuckDNS%20%2B%20Let's%20Encrypt-0f766e?style=flat-square)](#)

🌐 **Demo en vivo:** [sgsmarket.duckdns.org](https://sgsmarket.duckdns.org)

</div>

---

## ✨ Descripción

**SGS Market** es una plataforma web moderna y modular para la **administración integral de un supermercado**: catálogo, inventario, ventas, facturación, carrito de compras, pagos en línea, gestión de clientes, panel administrativo y observabilidad en tiempo real.

Construido con arquitectura limpia, principios SOLID y un pipeline CI/CD automatizado que entrega cambios a producción en minutos.

> 🎓 Proyecto desarrollado en la **Universidad del Quindío** como sistema real desplegado en producción.

---

## 📑 Tabla de contenido

- [✨ Descripción](#-descripción)
- [🚀 Features](#-features)
- [🧱 Arquitectura](#-arquitectura)
- [📁 Estructura de carpetas](#-estructura-de-carpetas)
- [⚙️ Stack tecnológico](#️-stack-tecnológico)
- [🔧 Instalación paso a paso](#-instalación-paso-a-paso)
- [🔐 Variables de entorno](#-variables-de-entorno)
- [📜 Scripts disponibles](#-scripts-disponibles)
- [🛰️ API Overview](#️-api-overview)
- [🖼️ Capturas](#️-capturas)
- [🐳 Monitoreo con Docker](#-monitoreo-con-docker)
- [☁️ Deployment](#️-deployment)
- [🗺️ Roadmap](#️-roadmap)
- [🤝 Contributing](#-contributing)
- [📄 Licencia](#-licencia)
- [👥 Créditos](#-créditos)

---

## 🚀 Features

| | Funcionalidad | Descripción |
|---|---|---|
| 📦 | **Gestión de inventario** | Control de stock, alertas de bajo inventario y movimientos |
| 🏷️ | **Gestión de productos** | CRUD completo con categorías, precios e imágenes |
| 💳 | **Ventas & facturación** | Procesamiento de pedidos y pagos con **MercadoPago** |
| 🛒 | **Carrito de compras** | Carrito persistente con cálculo de totales en vivo |
| 👥 | **Gestión de clientes** | Registro, verificación por email, direcciones múltiples |
| 📊 | **Dashboard administrativo** | Panel con KPIs, pedidos y alertas operativas |
| 📈 | **Reportes & estadísticas** | Métricas exportadas vía **Prometheus + Grafana** |
| 🔐 | **Roles de usuario** | `ADMIN`, `EMPLEADO`, `CLIENTE` con autorización por rol |
| 🔑 | **Autenticación JWT** | Tokens firmados + BCrypt para passwords |
| ✉️ | **Email transaccional** | Verificación de cuenta y recuperación de contraseña |
| 🧪 | **Tests automatizados** | JUnit (backend) + Cypress E2E (frontend) integrados a TestRail |
| ⚙️ | **CI/CD automatizado** | Pipeline Jenkins de 5 etapas con auto-trigger |

---

## 🧱 Arquitectura

```text
┌──────────────────────────────────────────────────────────────┐
│                    GCP Compute Engine (e2-small)             │
│                                                              │
│   ┌──────────┐    ┌──────────────────┐    ┌──────────────┐   │
│   │  Cliente │──► │  nginx 80/443    │──► │  Angular SPA │   │
│   │  Browser │    │  (reverse proxy) │    │  (static)    │   │
│   └──────────┘    └────────┬─────────┘    └──────────────┘   │
│                            │                                 │
│                            ▼                                 │
│                  ┌──────────────────┐    ┌──────────────┐    │
│                  │  Spring Boot     │──► │   MySQL 8    │    │
│                  │  REST API :8080  │    └──────────────┘    │
│                  └────────┬─────────┘                        │
│                           │                                  │
│        /actuator/prometheus│                                 │
│                           ▼                                  │
│   ┌──────────┐    ┌──────────────────┐    ┌──────────────┐   │
│   │ Jenkins  │    │  Prometheus      │──► │   Grafana    │   │
│   │  CI/CD   │    │  (docker)        │    │  (docker)    │   │
│   └──────────┘    └──────────────────┘    └──────────────┘   │
└──────────────────────────────────────────────────────────────┘
```

**Patrón:** API REST monolítica + SPA desacoplada — Controller → Service → Repository → JPA Entity.

---

## 📁 Estructura de carpetas

```text
sgs-project/
├── sgs-backend/                       # 🟢 Spring Boot REST API
│   ├── src/main/java/com/uniquindio/backend/
│   │   ├── config/                    # Beans, CORS, mail, Swagger
│   │   ├── controller/                # Endpoints REST
│   │   ├── dto/                       # Data Transfer Objects
│   │   ├── model/                     # Entidades JPA
│   │   ├── repository/                # Spring Data JPA
│   │   ├── security/                  # JWT + filtros + roles
│   │   ├── service/                   # Lógica de negocio
│   │   └── SgsBackendApplication.java
│   ├── src/test/java/...              # JUnit + Mockito
│   └── pom.xml
│
├── sgs-frontend/                      # 🔴 Angular 20 SPA
│   ├── src/app/
│   │   ├── core/                      # Guards, interceptors, services
│   │   ├── shared/                    # Componentes reutilizables
│   │   ├── pages/                     # Home, productos, carrito, admin…
│   │   └── app.routes.ts
│   ├── cypress/                       # Tests E2E
│   ├── proxy.conf.json
│   └── package.json
│
├── deploy/                            # ⚙️ Infraestructura
│   ├── nginx/                         # Configuración reverse proxy
│   ├── monitoring/                    # Prometheus + Grafana
│   ├── docker-compose.monitoring.yml
│   ├── sgs-backend.service            # systemd unit
│   ├── deploy.sh / deploy-jenkins.sh
│   └── seed-data*.sql
│
├── Jenkinsfile                        # 🟠 Pipeline CI/CD
├── PROYECTO.md                        # Resumen técnico
└── README.md
```

---

## ⚙️ Stack tecnológico

### Backend

| Tecnología | Versión | Uso |
|---|---|---|
| ☕ Java | 21 | Lenguaje |
| 🍃 Spring Boot | 3.x | Framework base |
| 🔐 Spring Security + JJWT | 0.12 | Auth con JWT + BCrypt |
| 🗄️ Spring Data JPA / Hibernate | — | ORM |
| 🐬 MySQL | 8 | Base de datos relacional |
| 💳 MercadoPago SDK | 2.8 | Pagos en línea |
| 📊 Actuator + Micrometer | — | Métricas Prometheus |
| ✉️ Spring Mail | — | Correo transaccional |
| 🧪 JUnit 5 + Mockito | — | Tests unitarios |

### Frontend

| Tecnología | Versión | Uso |
|---|---|---|
| 🅰️ Angular | 20 | SPA standalone + lazy loading |
| 🟦 TypeScript | 5.x | Lenguaje |
| 🧪 Cypress | 13 | Tests E2E |
| 🔗 cypress-testrail-simple | — | Reporte automático a TestRail |

### Infraestructura

| Herramienta | Uso |
|---|---|
| 🟠 Jenkins | Pipeline CI/CD (5 etapas, auto-trigger) |
| 🌐 nginx | Reverse proxy + HTTPS (Let's Encrypt) |
| 🦆 DuckDNS | DNS dinámico → IP estática GCP |
| 📈 Prometheus | Scraping de métricas |
| 📊 Grafana | Dashboards de observabilidad |
| 🐳 Docker Compose | Stack de monitoreo |
| 🔧 systemd | Servicio del backend |
| ☁️ GCP Compute Engine | Hosting (e2-small) |

---

## 🔧 Instalación paso a paso

### 📋 Prerrequisitos

- ☕ JDK **21+**
- 📦 Node.js **20+** y npm
- 🐬 MySQL **8+** corriendo localmente
- 🐙 Git

### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/sebastianagudelom/sgs-project.git
cd sgs-project
```

### 2️⃣ Backend — Spring Boot

```bash
cd sgs-backend

# Crear la base de datos
mysql -u root -p -e "CREATE DATABASE sgs_market;"

# (Opcional) cargar datos semilla
mysql -u root -p sgs_market < ../deploy/seed-data.sql

# Configurar variables (ver sección de entorno)
cp src/main/resources/application.example.properties \
   src/main/resources/application-local.properties

# Compilar y ejecutar
./mvnw clean spring-boot:run
```

🟢 Backend disponible en **http://localhost:8080**

### 3️⃣ Frontend — Angular

```bash
cd ../sgs-frontend
npm install
npm start
```

🔴 Frontend disponible en **http://localhost:4200** (proxy a `:8080`)

---

## 🔐 Variables de entorno

### Backend — `application-local.properties`

| Variable | Descripción | Ejemplo |
|---|---|---|
| `spring.datasource.url` | URL JDBC de MySQL | `jdbc:mysql://localhost:3306/sgs_market` |
| `spring.datasource.username` | Usuario MySQL | `root` |
| `spring.datasource.password` | Password MySQL | `••••••` |
| `jwt.secret` | Secreto para firmar JWT (≥ 256 bits) | `super-secret-key` |
| `jwt.expiration` | Expiración del token (ms) | `86400000` |
| `spring.mail.host` | SMTP host | `smtp.gmail.com` |
| `spring.mail.username` | Usuario SMTP | `tu@correo.com` |
| `spring.mail.password` | App password SMTP | `••••••` |
| `mercadopago.access.token` | Token de MercadoPago | `APP_USR-…` |
| `app.frontend.url` | URL del frontend (CORS) | `http://localhost:4200` |

### Frontend — `src/environments/environment.ts`

```ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
};
```

---

## 📜 Scripts disponibles

### Backend (Maven)

| Comando | Descripción |
|---|---|
| `./mvnw spring-boot:run` | Levanta la API en modo dev |
| `./mvnw clean package` | Build del JAR ejecutable |
| `./mvnw test` | Ejecuta tests unitarios |
| `./mvnw package -DskipTests` | Build sin tests |

### Frontend (npm)

| Comando | Descripción |
|---|---|
| `npm start` | Servidor dev en `:4200` |
| `npm run build` | Build de producción |
| `npm test` | Tests unitarios (Karma) |
| `npm run cypress:open` | Abre Cypress interactivo |
| `npm run cypress:run` | Ejecuta E2E en headless |

---

## 🛰️ API Overview

> Base URL: `http://localhost:8080/api`

### 🔐 Autenticación

| Método | Endpoint | Descripción | Rol |
|---|---|---|---|
| `POST` | `/auth/registro` | Registro de cliente | 🌐 público |
| `POST` | `/auth/login` | Login → JWT | 🌐 público |
| `POST` | `/auth/verificar` | Verificación por código | 🌐 público |

### 🏷️ Productos & Categorías

| Método | Endpoint | Descripción | Rol |
|---|---|---|---|
| `GET` | `/productos` | Listado paginado | 🌐 |
| `GET` | `/productos/{id}` | Detalle | 🌐 |
| `POST` | `/productos` | Crear producto | 🔴 ADMIN |
| `PUT` | `/productos/{id}` | Actualizar | 🔴 ADMIN |
| `DELETE` | `/productos/{id}` | Eliminar | 🔴 ADMIN |
| `GET` | `/categorias` | Listar categorías | 🌐 |

### 🛒 Carrito & Pedidos

| Método | Endpoint | Descripción | Rol |
|---|---|---|---|
| `GET` | `/carrito` | Carrito del usuario | 🟢 CLIENTE |
| `POST` | `/carrito/items` | Agregar producto | 🟢 |
| `DELETE` | `/carrito/items/{id}` | Eliminar item | 🟢 |
| `POST` | `/pedidos/checkout` | Crear pedido + pago | 🟢 |
| `GET` | `/pedidos/mios` | Historial de pedidos | 🟢 |

### 👥 Administración

| Método | Endpoint | Descripción | Rol |
|---|---|---|---|
| `GET` | `/admin/clientes` | Listar clientes | 🔴 ADMIN |
| `PATCH` | `/admin/clientes/{id}/estado` | Activar / inactivar | 🔴 |
| `GET` | `/admin/inventario/alertas` | Stock bajo | 🔴 |
| `GET` | `/admin/pedidos` | Todos los pedidos | 🔴 |

### 📊 Observabilidad

| Endpoint | Descripción |
|---|---|
| `/actuator/health` | Healthcheck |
| `/actuator/prometheus` | Métricas para Prometheus |

---

## 🖼️ Capturas

<div align="center">

| Home | Catálogo |
|:---:|:---:|
| ![home](https://via.placeholder.com/600x340/0ea5e9/ffffff?text=Home+%E2%80%94+Hero+%2B+Carruseles) | ![catalogo](https://via.placeholder.com/600x340/6366f1/ffffff?text=Cat%C3%A1logo+de+Productos) |

| Carrito | Dashboard Admin |
|:---:|:---:|
| ![carrito](https://via.placeholder.com/600x340/22c55e/ffffff?text=Carrito+%2B+Checkout+MercadoPago) | ![admin](https://via.placeholder.com/600x340/ef4444/ffffff?text=Panel+Administrativo) |

| Grafana | Pipeline Jenkins |
|:---:|:---:|
| ![grafana](https://via.placeholder.com/600x340/f97316/ffffff?text=Grafana+%E2%80%94+M%C3%A9tricas) | ![jenkins](https://via.placeholder.com/600x340/111827/ffffff?text=Jenkins+Pipeline+CI%2FCD) |

</div>

---

## 🐳 Monitoreo con Docker

El stack de **observabilidad** (Prometheus + Grafana) se levanta con Docker Compose. La aplicación corre nativa (systemd + nginx); Docker se usa **sólo** para monitoreo.

```bash
cd deploy
./setup-monitoring.sh
docker compose -f docker-compose.monitoring.yml up -d
```

| Servicio | Puerto | URL |
|---|---|---|
| Prometheus | `9090` | `http://localhost:9090` |
| Grafana | `3000` | `http://localhost:3000` |

> Prometheus scrapea `/actuator/prometheus` del backend cada 15s.

---

## ☁️ Deployment

Producción desplegada en **GCP Compute Engine** con dominio gratuito vía **DuckDNS** y certificados **Let's Encrypt**.

### Pipeline Jenkins (5 etapas)

```text
1. Backend  → Build       (mvnw clean package -DskipTests)
2. Backend  → Unit Tests  (mvnw test)
3. Frontend → Build       (npm ci && ng build)
4. Frontend → E2E         (Cypress + TestRail)
5. Deploy   → Rollout     (systemctl restart sgs-backend + nginx reload)
```

### Despliegue manual

```bash
cd deploy
./setup-server.sh        # Provisionar VM (java, mysql, nginx, jenkins)
./deploy.sh              # Deploy backend + frontend
```

---

## 🗺️ Roadmap

- [x] Autenticación JWT + roles
- [x] Pagos con MercadoPago
- [x] CI/CD con Jenkins
- [x] Monitoreo con Prometheus + Grafana
- [x] Tests E2E con Cypress + TestRail
- [ ] 📱 App móvil (Flutter / React Native)
- [ ] 🔔 Notificaciones push (FCM)
- [ ] 🤖 Recomendador de productos con ML
- [ ] 🌎 Internacionalización (i18n)
- [ ] 🧾 Facturación electrónica DIAN
- [ ] 🔍 Búsqueda fulltext con Elasticsearch

---

## 🤝 Contributing

¡Las contribuciones son bienvenidas! Para colaborar:

```bash
# 1. Forkea el repo
# 2. Crea una rama
git checkout -b feat/mi-feature

# 3. Commitea siguiendo Conventional Commits
git commit -m "feat: agrega filtro avanzado al catálogo"

# 4. Push y abre un Pull Request
git push origin feat/mi-feature
```

**Convenciones:**
- ✅ Conventional Commits (`feat:`, `fix:`, `chore:`, `docs:`…)
- ✅ Tests para nueva lógica
- ✅ Linter limpio (`ng lint`, sin warnings de Maven)

---

## 📄 Licencia

Distribuido bajo licencia **MIT**. Consulta [`LICENSE`](LICENSE) para más información.

```
Copyright (c) 2026 Sebastián Agudelo
```

---

## 👥 Créditos

<div align="center">

**Desarrollado con ❤️ por**

### Sebastián Agudelo
**Ingeniería de Sistemas y Computación — Universidad del Quindío**

[![GitHub](https://img.shields.io/badge/GitHub-sebastianagudelom-181717?style=for-the-badge&logo=github)](https://github.com/sebastianagudelom)
[![Email](https://img.shields.io/badge/Email-Contacto-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:sebastianagudelomendez@gmail.com)

---

⭐ Si este proyecto te resulta útil, considera darle una estrella en GitHub.

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:6366f1,100:0ea5e9&height=100&section=footer" alt="footer" />

</div>
