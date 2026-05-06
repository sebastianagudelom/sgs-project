import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { AuthService } from '../../services/auth.service';
import { CarritoService } from '../../services/carrito.service';
import { ProductoResponse } from '../../models/producto.model';
import { ProductReviewsComponent } from '../../components/product-reviews/product-reviews.component';

@Component({
  selector: 'app-producto-detalle',
  standalone: true,
  imports: [CommonModule, ProductReviewsComponent],
  templateUrl: './producto-detalle.component.html',
  styleUrl: './producto-detalle.component.css'
})
export class ProductoDetalleComponent implements OnInit {
  producto: ProductoResponse | null = null;
  loading = true;
  cantidad = 1;
  mensajeCarrito = '';
  isLoggedIn = false;
  isAdmin = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productoService: ProductoService,
    private authService: AuthService,
    private carritoService: CarritoService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.isAdmin = this.authService.isAdmin();
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.productoService.obtenerPorId(id).subscribe({
        next: (producto) => {
          this.producto = producto;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
          this.router.navigate(['/productos']);
        }
      });
    }
  }

  incrementar(): void {
    if (this.producto && this.cantidad < this.producto.stock) {
      this.cantidad++;
    }
  }

  decrementar(): void {
    if (this.cantidad > 1) {
      this.cantidad--;
    }
  }

  agregarAlCarrito(): void {
    if (!this.producto) return;
    try {
      this.carritoService.agregarItem({
        id: this.producto.id,
        nombre: this.producto.nombre,
        precio: this.producto.precio,
        imagenUrl: this.producto.imagenUrl,
        stock: this.producto.stock
      }, this.cantidad);
      this.mensajeCarrito = `"${this.producto.nombre}" x${this.cantidad} agregado al carrito`;
      this.cantidad = 1;
      setTimeout(() => this.mensajeCarrito = '', 2500);
    } catch (e: any) {
      this.mensajeCarrito = e.message;
      setTimeout(() => this.mensajeCarrito = '', 3000);
    }
  }

  volver(): void {
    this.router.navigate(['/productos']);
  }

  formatPrecio(precio: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0
    }).format(precio);
  }
}
