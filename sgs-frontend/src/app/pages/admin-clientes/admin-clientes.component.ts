import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import {
  ClienteResponse,
  ClienteDetalleResponse,
  ClienteAdminRequest
} from '../../models/cliente.model';
import { DireccionRequest, DireccionResponse } from '../../models/perfil.model';

type ModalTipo = 'crear' | 'editar' | 'password' | 'direcciones' | null;

@Component({
  selector: 'app-admin-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-clientes.component.html',
  styleUrl: './admin-clientes.component.css'
})
export class AdminClientesComponent implements OnInit {
  clientes: ClienteResponse[] = [];
  clientesFiltrados: ClienteResponse[] = [];
  loading = true;
  busqueda = '';
  filtroActividad = 'todos';

  // Modal control
  modalAbierto: ModalTipo = null;
  guardando = false;
  errorMsg = '';
  mensaje = '';

  // Cliente seleccionado
  clienteSeleccionado: ClienteResponse | null = null;

  // Form clientes (crear/editar)
  formCliente: ClienteAdminRequest = this.formClienteVacio();

  // Form password
  nuevaPassword = '';
  confirmarPassword = '';

  // Direcciones del cliente
  direccionesCliente: DireccionResponse[] = [];
  cargandoDirecciones = false;

  // Form dirección
  modoDireccion: 'lista' | 'crear' | 'editar' = 'lista';
  direccionEditando: DireccionResponse | null = null;
  formDireccion: DireccionRequest = this.formDireccionVacio();

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.cargarClientes();
  }

  // ===== Lista =====

  cargarClientes(): void {
    this.loading = true;
    this.adminService.listarClientes().subscribe({
      next: (data) => {
        this.clientes = data;
        this.filtrar();
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  filtrar(): void {
    let resultado = this.clientes;

    if (this.filtroActividad === 'con-pedidos') {
      resultado = resultado.filter(c => c.totalPedidos > 0);
    } else if (this.filtroActividad === 'sin-pedidos') {
      resultado = resultado.filter(c => c.totalPedidos === 0);
    } else if (this.filtroActividad === 'inactivos') {
      resultado = resultado.filter(c => !c.activo);
    } else if (this.filtroActividad === 'activos') {
      resultado = resultado.filter(c => c.activo);
    }

    if (this.busqueda.trim()) {
      const term = this.busqueda.trim().toLowerCase();
      resultado = resultado.filter(c =>
        c.nombre.toLowerCase().includes(term) ||
        c.apellido.toLowerCase().includes(term) ||
        c.email.toLowerCase().includes(term) ||
        (c.cedula && c.cedula.toLowerCase().includes(term))
      );
    }
    this.clientesFiltrados = resultado;
  }

  getTotalPedidos(): number {
    return this.clientesFiltrados.reduce((sum, c) => sum + c.totalPedidos, 0);
  }
  getTotalGastado(): number {
    return this.clientesFiltrados.reduce((sum, c) => sum + c.totalGastado, 0);
  }

  // ===== Acciones =====

  abrirCrear(): void {
    this.formCliente = this.formClienteVacio();
    this.errorMsg = '';
    this.modalAbierto = 'crear';
  }

  abrirEditar(cliente: ClienteResponse): void {
    this.clienteSeleccionado = cliente;
    this.formCliente = {
      nombre: cliente.nombre,
      apellido: cliente.apellido,
      email: cliente.email,
      cedula: cliente.cedula || '',
      telefono: cliente.telefono || ''
    };
    this.errorMsg = '';
    this.modalAbierto = 'editar';
  }

  abrirPassword(cliente: ClienteResponse): void {
    this.clienteSeleccionado = cliente;
    this.nuevaPassword = '';
    this.confirmarPassword = '';
    this.errorMsg = '';
    this.modalAbierto = 'password';
  }

  abrirDirecciones(cliente: ClienteResponse): void {
    this.clienteSeleccionado = cliente;
    this.modoDireccion = 'lista';
    this.errorMsg = '';
    this.modalAbierto = 'direcciones';
    this.cargarDirecciones(cliente.id);
  }

  cerrarModal(): void {
    this.modalAbierto = null;
    this.clienteSeleccionado = null;
    this.errorMsg = '';
    this.guardando = false;
  }

  // ===== Crear =====

  guardarNuevo(): void {
    this.errorMsg = '';
    if (!this.formCliente.password || this.formCliente.password.length < 6) {
      this.errorMsg = 'La contraseña debe tener al menos 6 caracteres';
      return;
    }
    this.guardando = true;
    this.adminService.crearCliente(this.formCliente).subscribe({
      next: () => {
        this.mostrarMensaje('Cliente creado');
        this.cerrarModal();
        this.cargarClientes();
      },
      error: (err) => {
        this.errorMsg = err.error?.mensaje || err.error?.message || 'No se pudo crear el cliente';
        this.guardando = false;
      }
    });
  }

  // ===== Editar =====

  guardarEdicion(): void {
    if (!this.clienteSeleccionado) return;
    this.errorMsg = '';
    this.guardando = true;
    const id = this.clienteSeleccionado.id;
    const req: ClienteAdminRequest = { ...this.formCliente };
    delete req.password;
    this.adminService.actualizarCliente(id, req).subscribe({
      next: () => {
        this.mostrarMensaje('Cliente actualizado');
        this.cerrarModal();
        this.cargarClientes();
      },
      error: (err) => {
        this.errorMsg = err.error?.mensaje || err.error?.message || 'No se pudo actualizar';
        this.guardando = false;
      }
    });
  }

  // ===== Cambiar estado =====

  cambiarEstado(cliente: ClienteResponse): void {
    const accion = cliente.activo ? 'desactivar' : 'activar';
    if (!confirm(`¿${accion} a ${cliente.nombre} ${cliente.apellido}?`)) return;
    this.adminService.cambiarEstadoCliente(cliente.id).subscribe({
      next: () => {
        this.mostrarMensaje(`Cliente ${accion === 'activar' ? 'activado' : 'desactivado'}`);
        this.cargarClientes();
      },
      error: (err) => {
        this.mostrarMensaje(err.error?.mensaje || 'Error al cambiar estado', true);
      }
    });
  }

  // ===== Reset password =====

  guardarPassword(): void {
    if (!this.clienteSeleccionado) return;
    this.errorMsg = '';
    if (this.nuevaPassword.length < 6) {
      this.errorMsg = 'La contraseña debe tener al menos 6 caracteres';
      return;
    }
    if (this.nuevaPassword !== this.confirmarPassword) {
      this.errorMsg = 'Las contraseñas no coinciden';
      return;
    }
    this.guardando = true;
    this.adminService.resetPasswordCliente(this.clienteSeleccionado.id, {
      nuevaPassword: this.nuevaPassword
    }).subscribe({
      next: () => {
        this.mostrarMensaje('Contraseña actualizada');
        this.cerrarModal();
      },
      error: (err) => {
        this.errorMsg = err.error?.mensaje || 'No se pudo cambiar la contraseña';
        this.guardando = false;
      }
    });
  }

  // ===== Direcciones =====

  cargarDirecciones(clienteId: number): void {
    this.cargandoDirecciones = true;
    this.adminService.listarDireccionesCliente(clienteId).subscribe({
      next: (data) => {
        this.direccionesCliente = data;
        this.cargandoDirecciones = false;
      },
      error: () => {
        this.cargandoDirecciones = false;
      }
    });
  }

  irNuevaDireccion(): void {
    this.formDireccion = this.formDireccionVacio();
    this.modoDireccion = 'crear';
    this.errorMsg = '';
  }

  irEditarDireccion(d: DireccionResponse): void {
    this.direccionEditando = d;
    this.formDireccion = {
      nombre: d.nombre,
      direccion: d.direccion,
      latitud: d.latitud,
      longitud: d.longitud,
      predeterminada: d.predeterminada
    };
    this.modoDireccion = 'editar';
    this.errorMsg = '';
  }

  cancelarFormDireccion(): void {
    this.modoDireccion = 'lista';
    this.direccionEditando = null;
    this.errorMsg = '';
  }

  guardarDireccion(): void {
    if (!this.clienteSeleccionado) return;
    this.errorMsg = '';
    this.guardando = true;
    const obs = this.modoDireccion === 'crear'
      ? this.adminService.crearDireccionCliente(this.clienteSeleccionado.id, this.formDireccion)
      : this.adminService.actualizarDireccionCliente(this.clienteSeleccionado.id, this.direccionEditando!.id, this.formDireccion);

    obs.subscribe({
      next: () => {
        this.mostrarMensaje('Dirección guardada');
        this.cargarDirecciones(this.clienteSeleccionado!.id);
        this.modoDireccion = 'lista';
        this.direccionEditando = null;
        this.guardando = false;
      },
      error: (err) => {
        this.errorMsg = err.error?.mensaje || 'No se pudo guardar';
        this.guardando = false;
      }
    });
  }

  eliminarDireccion(d: DireccionResponse): void {
    if (!this.clienteSeleccionado) return;
    if (!confirm(`¿Eliminar la dirección "${d.nombre}"?`)) return;
    this.adminService.eliminarDireccionCliente(this.clienteSeleccionado.id, d.id).subscribe({
      next: () => {
        this.mostrarMensaje('Dirección eliminada');
        this.cargarDirecciones(this.clienteSeleccionado!.id);
      },
      error: (err) => {
        this.mostrarMensaje(err.error?.mensaje || 'No se pudo eliminar', true);
      }
    });
  }

  // ===== Helpers =====

  private formClienteVacio(): ClienteAdminRequest {
    return { nombre: '', apellido: '', email: '', cedula: '', telefono: '', password: '' };
  }

  private formDireccionVacio(): DireccionRequest {
    return { nombre: '', direccion: '', latitud: null, longitud: null, predeterminada: false };
  }

  private mostrarMensaje(msg: string, esError = false): void {
    this.mensaje = (esError ? '⚠ ' : '✓ ') + msg;
    setTimeout(() => this.mensaje = '', 2800);
  }

  formatPrecio(precio: number): string {
    return new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', minimumFractionDigits: 0 }).format(precio);
  }

  formatFecha(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-CO', { year: 'numeric', month: 'short', day: 'numeric' });
  }
}
