import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { PedidoService } from '../../services/pedido.service';
import { CarritoService } from '../../services/carrito.service';
import { PedidoResponse, FacturaResponse } from '../../models/pedido.model';

const PENDING_ORDER_KEY = 'pendingOrderId';

@Component({
  selector: 'app-mis-pedidos',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './mis-pedidos.component.html',
  styleUrl: './mis-pedidos.component.css'
})
export class MisPedidosComponent implements OnInit {
  pedidos: PedidoResponse[] = [];
  loading = true;
  pedidoExpandido: number | null = null;
  pagoExitoso = false;

  facturaActual: FacturaResponse | null = null;
  mostrarFactura = false;
  cargandoFactura = false;

  constructor(
    private pedidoService: PedidoService,
    private carritoService: CarritoService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const status = params['status'] ?? params['collection_status'];
      if (status === 'approved') {
        this.carritoService.vaciarCarrito();
        localStorage.removeItem(PENDING_ORDER_KEY);
        this.pagoExitoso = true;
      }
    });
    this.cargarPedidos();
  }

  cargarPedidos(): void {
    this.loading = true;
    this.pedidoService.misPedidos().subscribe({
      next: (data) => {
        this.pedidos = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
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

  puedeVerFactura(estado: string): boolean {
    return ['PAGADO', 'CONFIRMADO', 'ENVIADO', 'ENTREGADO'].includes(estado);
  }

  verFactura(pedidoId: number): void {
    this.cargandoFactura = true;
    this.pedidoService.obtenerFactura(pedidoId).subscribe({
      next: (factura) => {
        this.facturaActual = factura;
        this.mostrarFactura = true;
        this.cargandoFactura = false;
      },
      error: () => {
        this.cargandoFactura = false;
      }
    });
  }

  cerrarFactura(): void {
    this.mostrarFactura = false;
    this.facturaActual = null;
  }

  imprimirFactura(): void {
    window.print();
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
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
