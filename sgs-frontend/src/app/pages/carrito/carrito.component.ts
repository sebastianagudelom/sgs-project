import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CarritoService } from '../../services/carrito.service';
import { PagoService } from '../../services/pago.service';
import { PedidoService } from '../../services/pedido.service';
import { PerfilService } from '../../services/perfil.service';
import { ItemCarrito, PedidoResponse } from '../../models/pedido.model';
import { DireccionResponse } from '../../models/perfil.model';

const PENDING_ORDER_KEY = 'pendingOrderId';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './carrito.component.html',
  styleUrl: './carrito.component.css'
})
export class CarritoComponent implements OnInit {
  direccionEnvio = '';
  errorMessage = '';
  successMessage = '';
  loading = false;

  direccionesGuardadas: DireccionResponse[] = [];
  direccionSeleccionadaId: number | null = null;
  usarDireccionPersonalizada = false;

  pedidoPendiente: PedidoResponse | null = null;
  cancelandoPendiente = false;

  constructor(
    public carritoService: CarritoService,
    private pagoService: PagoService,
    private pedidoService: PedidoService,
    private perfilService: PerfilService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.perfilService.listarDirecciones().subscribe({
      next: (dirs) => {
        this.direccionesGuardadas = dirs;
        const predeterminada = dirs.find(d => d.predeterminada);
        if (predeterminada) {
          this.direccionSeleccionadaId = predeterminada.id;
          this.direccionEnvio = predeterminada.direccion;
        } else if (dirs.length > 0) {
          this.direccionSeleccionadaId = dirs[0].id;
          this.direccionEnvio = dirs[0].direccion;
        }
      }
    });

    this.verificarPedidoPendiente();
  }

  private verificarPedidoPendiente(): void {
    const pendingId = localStorage.getItem(PENDING_ORDER_KEY);
    if (!pendingId) return;

    this.pedidoService.obtenerPorId(+pendingId).subscribe({
      next: (pedido) => {
        const estadosCompletados = ['PAGADO', 'CONFIRMADO', 'ENVIADO', 'ENTREGADO'];
        const estadosCancelados = ['CANCELADO', 'RECHAZADO'];

        if (estadosCompletados.includes(pedido.estado)) {
          this.carritoService.vaciarCarrito();
          localStorage.removeItem(PENDING_ORDER_KEY);
        } else if (estadosCancelados.includes(pedido.estado)) {
          localStorage.removeItem(PENDING_ORDER_KEY);
        } else {
          // Sigue PENDIENTE
          this.pedidoPendiente = pedido;
        }
      },
      error: () => {
        localStorage.removeItem(PENDING_ORDER_KEY);
      }
    });
  }

  cancelarPedidoPendiente(): void {
    if (!this.pedidoPendiente) return;
    this.cancelandoPendiente = true;
    this.pedidoService.cancelarPedido(this.pedidoPendiente.id).subscribe({
      next: () => {
        this.pedidoPendiente = null;
        localStorage.removeItem(PENDING_ORDER_KEY);
        this.cancelandoPendiente = false;
        this.successMessage = 'Pedido cancelado. Puedes realizar un nuevo pago.';
      },
      error: (err) => {
        this.cancelandoPendiente = false;
        this.errorMessage = err.error?.mensaje || err.error?.message || 'Error al cancelar el pedido';
      }
    });
  }

  seleccionarDireccion(id: number): void {
    this.usarDireccionPersonalizada = false;
    this.direccionSeleccionadaId = id;
    const dir = this.direccionesGuardadas.find(d => d.id === id);
    if (dir) {
      this.direccionEnvio = dir.direccion;
    }
  }

  usarOtraDireccion(): void {
    this.usarDireccionPersonalizada = true;
    this.direccionSeleccionadaId = null;
    this.direccionEnvio = '';
  }

  get items(): ItemCarrito[] {
    return this.carritoService.items;
  }

  actualizarCantidad(item: ItemCarrito, event: Event): void {
    const input = event.target as HTMLInputElement;
    const cantidad = parseInt(input.value, 10);
    if (isNaN(cantidad)) return;
    try {
      this.carritoService.actualizarCantidad(item.productoId, cantidad);
      this.errorMessage = '';
    } catch (e: any) {
      this.errorMessage = e.message;
      input.value = String(item.cantidad);
    }
  }

  cambiarCantidad(item: ItemCarrito, delta: number): void {
    const nuevaCantidad = item.cantidad + delta;
    if (nuevaCantidad < 1 || nuevaCantidad > item.stock) return;
    try {
      this.carritoService.actualizarCantidad(item.productoId, nuevaCantidad);
      this.errorMessage = '';
    } catch (e: any) {
      this.errorMessage = e.message;
    }
  }

  eliminarItem(productoId: number): void {
    this.carritoService.eliminarItem(productoId);
  }

  realizarPedido(): void {
    if (this.items.length === 0) {
      this.errorMessage = 'El carrito está vacío';
      return;
    }

    const dir = this.direccionEnvio.trim();
    if (!dir) {
      this.errorMessage = 'La dirección de envío es obligatoria';
      return;
    }
    if (dir.length < 10) {
      this.errorMessage = 'La dirección debe tener al menos 10 caracteres';
      return;
    }
    if (dir.length > 500) {
      this.errorMessage = 'La dirección no puede superar los 500 caracteres';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.pagoService.crearPreferencia({
      items: this.items.map(i => ({ productoId: i.productoId, cantidad: i.cantidad })),
      direccionEnvio: this.direccionEnvio
    }).subscribe({
      next: (response) => {
        // Guardar el pedidoId pendiente; el carrito se limpia solo al confirmar el pago
        localStorage.setItem(PENDING_ORDER_KEY, String(response.pedidoId));
        window.location.href = response.initPoint;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.mensaje || err.error?.message || 'Error al iniciar el pago';
      }
    });
  }

  formatPrecio(precio: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0
    }).format(precio);
  }
}
