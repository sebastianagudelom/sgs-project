# Evidencias de pruebas de metricas

Fecha de ejecucion: 2026-05-05, zona horaria America/Bogota.

## Alcance validado

| Modulo | Metrica | Evidencia |
| --- | --- | --- |
| Backend - clientes admin | `totalPedidos` y `totalGastado` por cliente | `AdminServiceTest` valida suma de pedidos y total acumulado por cliente. |
| Backend - inventario | Total de alertas activas | `InventarioServiceTest` valida que el contador use solo alertas `ACTIVA`. |
| Backend - inventario | Generacion y resolucion de alertas por stock bajo | `InventarioServiceTest` valida alerta activa cuando el stock esta bajo el umbral y alerta resuelta cuando el stock se recupera. |
| Backend - resenas | `promedio` y `total` de resenas por producto | `ResenaServiceTest` valida que el resumen devuelva promedio y cantidad del producto. |
| Frontend - clientes admin | Totales visibles de pedidos y dinero gastado | `admin-clientes.component.spec.ts` valida calculos con los clientes cargados. |
| Frontend - clientes admin | Recalculo de metricas con filtros | `admin-clientes.component.spec.ts` valida filtros con pedidos, sin pedidos, busqueda por nombre, apellido, email y cedula. |
| Frontend - clientes admin | Estados de carga y error | `admin-clientes.component.spec.ts` valida `ngOnInit`, carga exitosa y fallo del servicio. |
| Frontend - clientes admin | Formato de valores | `admin-clientes.component.spec.ts` valida formato de moneda COP y fecha visible. |

## Comandos ejecutados

### Backend

Directorio:

```bash
sgs-backend
```

Comando:

```bash
./mvnw -Dtest=AdminServiceTest,InventarioServiceTest,ResenaServiceTest test
```

Resultado:

```text
BUILD SUCCESS
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

Detalle generado por Surefire:

```text
AdminServiceTest: Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
InventarioServiceTest: Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
ResenaServiceTest: Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

### Frontend

Directorio:

```bash
sgs-frontend
```

Comando:

```bash
env CHROME_BIN="/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" npm test -- --watch=false --browsers=ChromeHeadless
```

Resultado:

```text
Chrome Headless 148.0.0.0: Executed 11 of 11 SUCCESS
TOTAL: 11 SUCCESS
```

## Notas tecnicas

- El primer intento de `ng test` dentro del sandbox se corto al construir/lanzar el navegador. Se ejecuto fuera del sandbox con aprobacion porque Karma necesita iniciar Google Chrome en modo headless.
- Se agrego `src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker` en backend con `mock-maker-subclass` para evitar el fallo del agente inline de Mockito en el JDK local. Las pruebas solo mockean interfaces de repositorio, por lo que ese mock maker es suficiente.
