import { throwError, of } from 'rxjs';

import { ClienteResponse } from '../../models/cliente.model';
import { AdminService } from '../../services/admin.service';
import { AdminClientesComponent } from './admin-clientes.component';

describe('AdminClientesComponent metricas', () => {
  let adminService: jasmine.SpyObj<AdminService>;
  let component: AdminClientesComponent;

  beforeEach(() => {
    adminService = jasmine.createSpyObj<AdminService>('AdminService', ['listarClientes']);
    component = new AdminClientesComponent(adminService);
  });

  it('carga clientes y calcula pedidos totales y total gastado', () => {
    adminService.listarClientes.and.returnValue(of(clientesPrueba));

    component.cargarClientes();

    expect(component.loading).toBeFalse();
    expect(component.clientesFiltrados.length).toBe(3);
    expect(component.getTotalPedidos()).toBe(5);
    expect(component.getTotalGastado()).toBe(380000);
  });

  it('actualiza las metricas al filtrar clientes con pedidos y busqueda', () => {
    component.clientes = clientesPrueba;
    component.filtroActividad = 'con-pedidos';
    component.busqueda = 'ana';

    component.filtrar();

    expect(component.clientesFiltrados.map(cliente => cliente.email)).toEqual(['ana@test.com']);
    expect(component.getTotalPedidos()).toBe(2);
    expect(component.getTotalGastado()).toBe(180000);
  });

  it('deja las metricas en cero al filtrar clientes sin pedidos', () => {
    component.clientes = clientesPrueba;
    component.filtroActividad = 'sin-pedidos';

    component.filtrar();

    expect(component.clientesFiltrados.map(cliente => cliente.email)).toEqual(['luis@test.com']);
    expect(component.getTotalPedidos()).toBe(0);
    expect(component.getTotalGastado()).toBe(0);
  });

  it('carga clientes al inicializar el componente', () => {
    adminService.listarClientes.and.returnValue(of(clientesPrueba));

    component.ngOnInit();

    expect(adminService.listarClientes).toHaveBeenCalledTimes(1);
    expect(component.loading).toBeFalse();
    expect(component.clientes).toEqual(clientesPrueba);
    expect(component.clientesFiltrados).toEqual(clientesPrueba);
  });

  it('desactiva loading y conserva listas vacias cuando falla la carga', () => {
    adminService.listarClientes.and.returnValue(
      throwError(() => new Error('Error cargando clientes'))
    );

    component.cargarClientes();

    expect(component.loading).toBeFalse();
    expect(component.clientes).toEqual([]);
    expect(component.clientesFiltrados).toEqual([]);
  });

  it('filtra sin diferenciar mayusculas ni espacios al inicio o final', () => {
    component.clientes = clientesPrueba;
    component.busqueda = '  MARTA  ';

    component.filtrar();

    expect(component.clientesFiltrados.map(cliente => cliente.email)).toEqual(['marta@test.com']);
    expect(component.getTotalPedidos()).toBe(3);
    expect(component.getTotalGastado()).toBe(200000);
  });

  it('filtra por apellido, email y cedula', () => {
    component.clientes = clientesPrueba;

    component.busqueda = 'perez';
    component.filtrar();
    expect(component.clientesFiltrados.map(cliente => cliente.email)).toEqual(['luis@test.com']);

    component.busqueda = 'ana@test.com';
    component.filtrar();
    expect(component.clientesFiltrados.map(cliente => cliente.email)).toEqual(['ana@test.com']);

    component.busqueda = '789';
    component.filtrar();
    expect(component.clientesFiltrados.map(cliente => cliente.email)).toEqual(['marta@test.com']);
  });

  it('filtra todos los clientes con pedidos cuando no hay busqueda', () => {
    component.clientes = clientesPrueba;
    component.filtroActividad = 'con-pedidos';

    component.filtrar();

    expect(component.clientesFiltrados.map(cliente => cliente.email)).toEqual([
      'ana@test.com',
      'marta@test.com'
    ]);
    expect(component.getTotalPedidos()).toBe(5);
    expect(component.getTotalGastado()).toBe(380000);
  });

  it('deja la lista vacia y metricas en cero si la busqueda no coincide', () => {
    component.clientes = clientesPrueba;
    component.busqueda = 'cliente inexistente';

    component.filtrar();

    expect(component.clientesFiltrados).toEqual([]);
    expect(component.getTotalPedidos()).toBe(0);
    expect(component.getTotalGastado()).toBe(0);
  });

  it('formatea precios en pesos colombianos sin decimales', () => {
    expect(component.formatPrecio(380000)).toBe('$\u00a0380.000');
  });

  it('formatea fechas con locale colombiano', () => {
    expect(component.formatFecha('2026-05-05T09:00:00')).toContain('2026');
    expect(component.formatFecha('2026-05-05T09:00:00')).toContain('5');
  });
});

const clientesPrueba: ClienteResponse[] = [
  {
    id: 1,
    nombre: 'Ana',
    apellido: 'Gomez',
    email: 'ana@test.com',
    cedula: '123',
    telefono: '3001112233',
    activo: true,
    totalPedidos: 2,
    totalGastado: 180000,
    fechaRegistro: '2026-05-05T09:00:00'
  },
  {
    id: 2,
    nombre: 'Luis',
    apellido: 'Perez',
    email: 'luis@test.com',
    cedula: '456',
    telefono: '3004445566',
    activo: true,
    totalPedidos: 0,
    totalGastado: 0,
    fechaRegistro: '2026-05-05T10:00:00'
  },
  {
    id: 3,
    nombre: 'Marta',
    apellido: 'Lopez',
    email: 'marta@test.com',
    cedula: '789',
    telefono: '3007778899',
    activo: false,
    totalPedidos: 3,
    totalGastado: 200000,
    fechaRegistro: '2026-05-05T11:00:00'
  }
];
