# SGS Market — Backend

> API REST desarrollada con **Spring Boot 4.0.3** (Java 21) para un supermercado en linea. Incluye autenticacion JWT, verificacion por correo, gestion de productos/categorias/pedidos, pagos con MercadoPago Checkout Pro, perfil de usuario con direcciones, y panel de administracion.

**Produccion:** [https://sgsmarket.duckdns.org](https://sgsmarket.duckdns.org)

---

## Tabla de Contenidos

- [Caracteristicas](#caracteristicas)
- [Tecnologias](#tecnologias)
- [Requisitos Previos](#requisitos-previos)
- [Configuracion](#configuracion)
- [Instalacion y Ejecucion](#instalacion-y-ejecucion)
- [Despliegue en AWS](#despliegue-en-aws)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Endpoints de la API](#endpoints-de-la-api)
- [Seguridad](#seguridad)
- [Usuario Administrador por Defecto](#usuario-administrador-por-defecto)

---

## Caracteristicas

- Registro de usuarios con verificacion por correo electronico (codigo de 6 digitos, expira en 15 min)
- Autenticacion stateless mediante JWT (expiracion 24 horas)
- Control de acceso por roles: `ADMIN` y `CLIENTE`
- CRUD completo de Productos con activacion/desactivacion (soft delete) y eliminacion definitiva con proteccion de FK
- CRUD completo de Categorias
- Sistema de Pedidos con 7 estados: PENDIENTE, PAGADO, CONFIRMADO, ENVIADO, ENTREGADO, CANCELADO, RECHAZADO
- Pagos integrados con MercadoPago Checkout Pro (webhook IPN)
- Generacion de facturas por pedido
- Perfil de usuario con gestion de direcciones (CRUD + predeterminada)
- Panel de administracion: gestion de clientes con estadisticas (total pedidos, total gastado)
- Subida de imagenes para productos
- CORS configurable via variables de entorno para produccion
- Creacion automatica de la base de datos y usuario administrador al iniciar
- Perfil de produccion (`application-prod.properties`) con variables de entorno externalizadas

---

## Tecnologias

| Tecnologia | Version |
|---|---|
| Java | 21 (Temurin) |
| Spring Boot | 4.0.3 |
| Spring Security | 6.x |
| Spring Data JPA | 3.x |
| Hibernate | 7.2.4 |
| MySQL | 8.x / 9.x |
| JJWT | 0.12.6 |
| MercadoPago SDK | 2.8.0 |
| Lombok | latest |
| Maven | 3.9.x |

---

## Requisitos Previos

- **JDK 21** o superior
- **MySQL 8+** corriendo en `localhost:3306`
- **Maven 3.9+** (o usar el wrapper incluido `./mvnw`)
- Cuenta de **Gmail** con [App Password](https://myaccount.google.com/apppasswords) para envio de correos
- Cuenta de **MercadoPago** con access token para pagos

---

## Configuracion

### Desarrollo

Edita `src/main/resources/application.properties`:

```properties
# Base de Datos
spring.datasource.username=TU_USUARIO_MYSQL
spring.datasource.password=TU_PASSWORD_MYSQL

# JWT
jwt.secret=TU_SECRET_BASE64

# Correo (Gmail)
spring.mail.username=TU_CORREO@gmail.com
spring.mail.password=TU_APP_PASSWORD

# MercadoPago
mercadopago.access-token=TU_ACCESS_TOKEN
mercadopago.notification-url=TU_URL_WEBHOOK
```

### Produccion

El perfil `prod` usa variables de entorno definidas en `/opt/sgs/.env`:

```bash
DB_USERNAME=sgs_user
DB_PASSWORD=...
JWT_SECRET=...
MAIL_USERNAME=...
MAIL_PASSWORD=...
MP_ACCESS_TOKEN=...
MP_NOTIFICATION_URL=https://sgsmarket.duckdns.org/api/pagos/webhook
CORS_ALLOWED_ORIGINS=https://sgsmarket.duckdns.org
```

> **Nota:** No subas credenciales reales al repositorio.

---

## Instalacion y Ejecucion

```bash
# Clonar el repositorio
git clone <url-del-repo>
cd sgs-backend

# Compilar
./mvnw clean compile

# Ejecutar (desarrollo)
./mvnw spring-boot:run

# Ejecutar (produccion)
java -jar target/sgs-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

La API queda disponible en **`https://localhost:8443`** (desarrollo) o **`http://localhost:8080`** (produccion, detras de Nginx).

---

## Despliegue en AWS

El proyecto incluye scripts de despliegue en la carpeta `deploy/`:

| Archivo | Descripcion |
|---|---|
| `setup-server.sh` | Configura EC2 Ubuntu: instala Java 21, MySQL, Nginx, SSL, systemd |
| `deploy.sh` | Compila y sube backend + frontend al servidor |
| `nginx/sgs.conf` | Reverse proxy con HTTPS (Let's Encrypt) |
| `sgs-backend.service` | Servicio systemd para Spring Boot |
| `.env.example` | Template de variables de entorno |

```bash
# Configuracion inicial del servidor
bash deploy/setup-server.sh

# Desplegar (desde tu maquina local)
bash deploy/deploy.sh <IP_EC2> <PEM_FILE>
```

Arquitectura: Nginx (HTTPS :443) -> Spring Boot (HTTP :8080 interno) + archivos Angular estaticos.

---

## Estructura del Proyecto

```
src/main/java/com/uniquindio/backend/
|-- config/
|   |-- CorsConfig.java              # CORS configurable via propiedad
|   |-- DataInitializer.java         # Crea admin por defecto al iniciar
|   |-- GlobalExceptionHandler.java  # Manejo centralizado de errores
|   |-- JwtProperties.java           # @ConfigurationProperties para JWT
|   |-- MercadoPagoSetup.java        # Inicializa SDK de MercadoPago
|   +-- WebConfig.java               # Sirve archivos subidos (/uploads)
|-- controller/
|   |-- AdminController.java         # /api/admin/** (clientes)
|   |-- AuthController.java          # /api/auth/** (registro, login, verificacion)
|   |-- CategoriaController.java     # /api/categorias/**
|   |-- ImagenController.java        # /api/imagenes/** (upload/descarga)
|   |-- PagoController.java          # /api/pagos/** (MercadoPago)
|   |-- PedidoController.java        # /api/pedidos/** (CRUD + factura)
|   |-- PerfilController.java        # /api/perfil/** (datos + direcciones)
|   +-- ProductoController.java      # /api/productos/** (CRUD + toggle)
|-- dto/                             # 20 DTOs (Request/Response)
|-- model/
|   |-- Categoria.java
|   |-- DetallePedido.java
|   |-- Direccion.java
|   |-- EstadoPedido.java            # Enum: 7 estados
|   |-- Imagen.java
|   |-- Pedido.java
|   |-- Producto.java
|   |-- Rol.java                     # Enum: ADMIN, CLIENTE
|   +-- Usuario.java
|-- repository/                      # 7 repositorios JPA
|-- security/
|   |-- JwtAuthFilter.java           # Filtro de autenticacion JWT
|   |-- JwtService.java              # Generacion y validacion de tokens
|   +-- SecurityConfig.java          # Cadena de seguridad HTTP
+-- service/                         # 9 servicios de negocio
```

---

## Endpoints de la API

### Autenticacion — `/api/auth`

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| `POST` | `/api/auth/registro` | Publico | Registra usuario (nombre, apellido, email, password, cedula, telefono) |
| `POST` | `/api/auth/login` | Publico | Inicia sesion, retorna JWT |
| `POST` | `/api/auth/verificar` | Publico | Verifica cuenta con codigo de 6 digitos |
| `POST` | `/api/auth/reenviar-codigo` | Publico | Reenvia codigo de verificacion |

### Productos — `/api/productos`

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| `GET` | `/api/productos` | Publico | Lista productos activos |
| `GET` | `/api/productos/todos` | Publico | Lista todos los productos (incluye inactivos) |
| `GET` | `/api/productos/{id}` | Publico | Obtiene producto por ID |
| `GET` | `/api/productos/buscar?nombre=` | Publico | Busca por nombre |
| `GET` | `/api/productos/categoria/{id}` | Publico | Filtra por categoria |
| `POST` | `/api/productos` | ADMIN | Crea producto |
| `PUT` | `/api/productos/{id}` | ADMIN | Actualiza producto |
| `PATCH` | `/api/productos/{id}/toggle-activo` | ADMIN | Activa/desactiva producto |
| `DELETE` | `/api/productos/{id}` | ADMIN | Elimina definitivamente (proteccion FK) |

### Categorias — `/api/categorias`

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| `GET` | `/api/categorias` | Publico | Lista todas |
| `GET` | `/api/categorias/{id}` | Publico | Obtiene por ID |
| `POST` | `/api/categorias` | ADMIN | Crea categoria |
| `PUT` | `/api/categorias/{id}` | ADMIN | Actualiza categoria |
| `DELETE` | `/api/categorias/{id}` | ADMIN | Elimina categoria |

### Pedidos — `/api/pedidos`

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| `POST` | `/api/pedidos` | Autenticado | Crea pedido desde carrito |
| `GET` | `/api/pedidos/mis-pedidos` | Autenticado | Lista pedidos del usuario |
| `GET` | `/api/pedidos` | ADMIN | Lista todos los pedidos |
| `GET` | `/api/pedidos/{id}` | Autenticado | Detalle de un pedido |
| `PATCH` | `/api/pedidos/{id}/estado` | ADMIN | Cambia estado del pedido |
| `GET` | `/api/pedidos/{id}/factura` | Autenticado | Genera factura del pedido |

### Pagos — `/api/pagos`

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| `POST` | `/api/pagos/crear` | Autenticado | Crea preferencia MercadoPago |
| `POST` | `/api/pagos/webhook` | Publico | Recibe notificaciones IPN |

### Perfil — `/api/perfil`

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| `GET` | `/api/perfil` | Autenticado | Obtiene datos del perfil |
| `PUT` | `/api/perfil` | Autenticado | Actualiza nombre, apellido, telefono |
| `GET` | `/api/perfil/direcciones` | Autenticado | Lista direcciones |
| `POST` | `/api/perfil/direcciones` | Autenticado | Agrega direccion |
| `PUT` | `/api/perfil/direcciones/{id}` | Autenticado | Actualiza direccion |
| `DELETE` | `/api/perfil/direcciones/{id}` | Autenticado | Elimina direccion |
| `PATCH` | `/api/perfil/direcciones/{id}/predeterminada` | Autenticado | Marca como predeterminada |

### Imagenes — `/api/imagenes`

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| `POST` | `/api/imagenes/upload` | ADMIN | Sube imagen de producto |
| `GET` | `/api/imagenes/{id}` | Publico | Descarga imagen |

### Administracion — `/api/admin`

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| `GET` | `/api/admin/clientes` | ADMIN | Lista clientes con estadisticas |

---

## Seguridad

Los endpoints protegidos requieren el header:

```
Authorization: Bearer <token_jwt>
```

El flujo de registro es:
1. `POST /api/auth/registro` — Se crea usuario inactivo y se envia codigo al correo
2. `POST /api/auth/verificar` — Se activa la cuenta (codigo valido 15 min)
3. `POST /api/auth/login` — Se obtiene el JWT (24h de expiracion)

---

## Usuario Administrador por Defecto

Al iniciar por primera vez se crea automaticamente:

| Campo | Valor |
|---|---|
| Email | `admin@sgssupermercado.com` |
| Password | `Admin123` |
| Rol | `ADMIN` |

---

## Proyecto Universitario

Desarrollado para la asignatura **Software 3** — Universidad del Quindio.
Frontend: [sgs-frontend](../sgs-frontend/)

---

## Proyecto Universitario

Desarrollado para la asignatura **Software 3** — Universidad del Quindío.
