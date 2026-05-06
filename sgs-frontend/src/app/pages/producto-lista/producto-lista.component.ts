import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { AuthService } from '../../services/auth.service';
import { CarritoService } from '../../services/carrito.service';
import { CategoriaService } from '../../services/categoria.service';
import { ProductoResponse } from '../../models/producto.model';
import { CategoriaResponse } from '../../models/categoria.model';

@Component({
  selector: 'app-producto-lista',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './producto-lista.component.html',
  styleUrl: './producto-lista.component.css'
})
export class ProductoListaComponent implements OnInit {
  productos: ProductoResponse[] = [];
  productosFiltrados: ProductoResponse[] = [];
  categorias: CategoriaResponse[] = [];
  categoriaSeleccionada: number | null = null;
  busqueda = '';
  loading = true;
  isAdmin = false;
  isLoggedIn = false;
  mensajeCarrito = '';
  cantidades: { [productoId: number]: number } = {};

  constructor(
    private productoService: ProductoService,
    private authService: AuthService,
    private carritoService: CarritoService,
    private categoriaService: CategoriaService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.isAdmin();
    this.isLoggedIn = this.authService.isLoggedIn();
    this.cargarCategorias();
    this.cargarProductos();

    this.route.queryParams.subscribe(params => {
      if (params['buscar']) {
        this.busqueda = params['buscar'];
        this.filtrar();
      }
    });
  }

  cargarCategorias(): void {
    this.categoriaService.listarTodas().subscribe({
      next: (data) => this.categorias = data,
      error: () => {}
    });
  }

  cargarProductos(): void {
    this.loading = true;
    const obs = this.isAdmin
      ? this.productoService.listarTodos()
      : this.productoService.listarActivos();

    obs.subscribe({
      next: (data) => {
        this.productos = data;
        this.productosFiltrados = data;
        this.loading = false;
        if (this.busqueda) this.filtrar();
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  filtrarPorCategoria(categoriaId: number | null): void {
    this.categoriaSeleccionada = categoriaId;
    this.filtrar();
  }

  buscar(): void {
    this.filtrar();
  }

  private filtrar(): void {
    let resultado = this.productos;

    if (this.categoriaSeleccionada) {
      resultado = resultado.filter(p => p.categoriaId === this.categoriaSeleccionada);
    }

    if (this.busqueda.trim()) {
      const term = this.busqueda.toLowerCase();
      resultado = resultado.filter(p =>
        p.nombre.toLowerCase().includes(term) ||
        p.categoriaNombre.toLowerCase().includes(term)
      );
    }

    this.productosFiltrados = resultado;
  }

  agregarProducto(): void {
    this.router.navigate(['/productos/nuevo']);
  }

  editarProducto(id: number): void {
    this.router.navigate(['/productos/editar', id]);
  }

  eliminarProducto(id: number): void {
    if (confirm('Esta accion eliminara el producto permanentemente. ¿Continuar?')) {
      this.productoService.eliminar(id).subscribe({
        next: () => this.cargarProductos(),
        error: (err) => {
          alert(err.error?.message || err.error?.mensaje || 'No se puede eliminar el producto');
        }
      });
    }
  }

  toggleActivo(producto: ProductoResponse): void {
    const accion = producto.activo ? 'desactivar' : 'activar';
    if (confirm(`¿Deseas ${accion} el producto "${producto.nombre}"?`)) {
      this.productoService.toggleActivo(producto.id).subscribe({
        next: () => this.cargarProductos()
      });
    }
  }

  agregarAlCarrito(producto: ProductoResponse): void {
    const cantidad = this.getCantidad(producto);
    try {
      this.carritoService.agregarItem({
        id: producto.id,
        nombre: producto.nombre,
        precio: producto.precio,
        imagenUrl: producto.imagenUrl,
        stock: producto.stock
      }, cantidad);
      this.mensajeCarrito = `"${producto.nombre}" x${cantidad} agregado al carrito`;
      this.cantidades[producto.id] = 1;
      setTimeout(() => this.mensajeCarrito = '', 2500);
    } catch (e: any) {
      this.mensajeCarrito = e.message;
      setTimeout(() => this.mensajeCarrito = '', 3000);
    }
  }

  getCantidad(producto: ProductoResponse): number {
    return this.cantidades[producto.id] || 1;
  }

  incrementarCantidad(producto: ProductoResponse): void {
    const actual = this.getCantidad(producto);
    if (actual < producto.stock) {
      this.cantidades[producto.id] = actual + 1;
    }
  }

  decrementarCantidad(producto: ProductoResponse): void {
    const actual = this.getCantidad(producto);
    if (actual > 1) {
      this.cantidades[producto.id] = actual - 1;
    }
  }

  verDetalle(id: number): void {
    this.router.navigate(['/productos', id]);
  }

  formatPrecio(precio: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0
    }).format(precio);
  }
}
