import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PedidoService } from '../../services/pedido.service';
import { PedidoResponse } from '../../models/pedido.model';

@Component({
  selector: 'app-admin-pedidos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-pedidos.component.html',
  styleUrl: './admin-pedidos.component.css'
})
export class AdminPedidosComponent implements OnInit {
  pedidos: PedidoResponse[] = [];
  loading = true;
  errorMessage = '';
  pedidoExpandido: number | null = null;
  private static readonly ORDEN_ESTADO: Record<string, number> = {
    PENDIENTE: 0, PAGADO: 1, CONFIRMADO: 2, ENVIADO: 3, ENTREGADO: 4
  };
  private static readonly ESTADOS_TERMINALES = new Set(['ENTREGADO', 'CANCELADO', 'RECHAZADO']);

  constructor(private pedidoService: PedidoService) {}

  ngOnInit(): void {
    this.cargarPedidos();
  }

  cargarPedidos(): void {
    this.loading = true;
    this.pedidoService.listarTodos().subscribe({
      next: (data) => {
        this.pedidos = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  cambiarEstado(pedidoId: number, nuevoEstado: string): void {
    this.errorMessage = '';
    this.pedidoService.actualizarEstado(pedidoId, nuevoEstado).subscribe({
      next: (updated) => {
        const idx = this.pedidos.findIndex(p => p.id === pedidoId);
        if (idx >= 0) {
          this.pedidos[idx] = updated;
        }
      },
      error: (err) => {
        this.errorMessage = err.error?.mensaje || 'Error al cambiar estado';
      }
    });
  }

  toggleDetalles(pedidoId: number): void {
    this.pedidoExpandido = this.pedidoExpandido === pedidoId ? null : pedidoId;
  }

  getEstadoClass(estado: string): string {
    switch (estado) {
      case 'PENDIENTE': return 'estado-pendiente';
      case 'PAGADO': return 'estado-pagado';
      case 'CONFIRMADO': return 'estado-confirmado';
      case 'ENVIADO': return 'estado-enviado';
      case 'ENTREGADO': return 'estado-entregado';
      case 'CANCELADO': return 'estado-cancelado';
      case 'RECHAZADO': return 'estado-rechazado';
      default: return '';
    }
  }

  esEstadoTerminal(estado: string): boolean {
    return AdminPedidosComponent.ESTADOS_TERMINALES.has(estado);
  }

  getEstadosPermitidos(estadoActual: string): string[] {
    const ordenActual = AdminPedidosComponent.ORDEN_ESTADO[estadoActual] ?? -1;
    const avance = Object.entries(AdminPedidosComponent.ORDEN_ESTADO)
      .filter(([, orden]) => orden > ordenActual)
      .map(([est]) => est);
    return [estadoActual, ...avance, 'CANCELADO'];
  }

  getEstadoLabel(estado: string): string {
    switch (estado) {
      case 'PENDIENTE': return 'Pendiente';
      case 'PAGADO': return 'Pagado';
      case 'CONFIRMADO': return 'Confirmado';
      case 'ENVIADO': return 'Enviado';
      case 'ENTREGADO': return 'Entregado';
      case 'CANCELADO': return 'Cancelado';
      case 'RECHAZADO': return 'Rechazado';
      default: return estado;
    }
  }

  formatPrecio(precio: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0
    }).format(precio);
  }

  formatFecha(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-CO', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
