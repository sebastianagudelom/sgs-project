import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { CategoriaService } from '../../services/categoria.service';
import { AuthService } from '../../services/auth.service';
import { CarritoService } from '../../services/carrito.service';
import { ProductoResponse } from '../../models/producto.model';
import { CategoriaResponse } from '../../models/categoria.model';

interface CarruselCategoria {
  categoria: CategoriaResponse;
  productos: ProductoResponse[];
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  busqueda = '';
  loading = true;

  categorias: CategoriaResponse[] = [];
  productos: ProductoResponse[] = [];

  destacados: ProductoResponse[] = [];
  novedades: ProductoResponse[] = [];
  carruselesPorCategoria: CarruselCategoria[] = [];

  isLoggedIn = false;
  mensajeCarrito = '';

  // Iconos visuales para categorías (mapeo nombre → emoji/icono)
  private categoriaIconos: { [key: string]: string } = {
    'Lacteos': '🥛',
    'Lácteos': '🥛',
    'Granos': '🌾',
    'Frutas': '🍎',
    'Panaderia': '🥖',
    'Panadería': '🥖',
    'Bebidas': '🥤',
    'Carnes': '🥩',
    'Verduras': '🥕',
    'Snacks': '🍿',
    'Aseo': '🧴',
    'Limpieza': '🧼',
    'Enlatados': '🥫',
    'Mascotas': '🐾',
    'Bebés': '🍼',
    'Bebes': '🍼',
    'Congelados': '🧊',
    'Dulces': '🍬'
  };

  constructor(
    private productoService: ProductoService,
    private categoriaService: CategoriaService,
    private authService: AuthService,
    private carritoService: CarritoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.cargar();
  }

  private cargar(): void {
    this.loading = true;
    this.categoriaService.listarTodas().subscribe({
      next: (cats) => {
        this.categorias = cats;
        this.productoService.listarActivos().subscribe({
          next: (prods) => {
            this.productos = prods.filter(p => p.activo);
            this.armarSecciones();
            this.loading = false;
          },
          error: () => { this.loading = false; }
        });
      },
      error: () => { this.loading = false; }
    });
  }

  private armarSecciones(): void {
    // Destacados: 8 productos con stock alto (más populares por proxy)
    this.destacados = [...this.productos]
      .filter(p => p.stock > 5)
      .sort((a, b) => b.stock - a.stock)
      .slice(0, 8);

    // Novedades: últimos 8 por fecha de creación
    this.novedades = [...this.productos]
      .sort((a, b) => new Date(b.fechaCreacion).getTime() - new Date(a.fechaCreacion).getTime())
      .slice(0, 8);

    // Un carrusel por categoría (solo las que tengan productos)
    this.carruselesPorCategoria = this.categorias
      .map(cat => ({
        categoria: cat,
        productos: this.productos
          .filter(p => p.categoriaId === cat.id && p.stock > 0)
          .slice(0, 10)
      }))
      .filter(c => c.productos.length > 0);
  }

  buscar(): void {
    if (this.busqueda.trim()) {
      this.router.navigate(['/productos'], { queryParams: { buscar: this.busqueda.trim() } });
    } else {
      this.router.navigate(['/productos']);
    }
  }

  irACategoria(categoriaId: number): void {
    this.router.navigate(['/productos'], { queryParams: { categoria: categoriaId } });
  }

  verTodosProductos(): void {
    this.router.navigate(['/productos']);
  }

  verDetalle(id: number): void {
    this.router.navigate(['/productos', id]);
  }

  iconoCategoria(nombre: string): string {
    return this.categoriaIconos[nombre] || '🛒';
  }

  agregarAlCarrito(producto: ProductoResponse, event: Event): void {
    event.stopPropagation();
    if (!this.isLoggedIn) {
      this.router.navigate(['/login']);
      return;
    }
    try {
      this.carritoService.agregarItem({
        id: producto.id,
        nombre: producto.nombre,
        precio: producto.precio,
        imagenUrl: producto.imagenUrl,
        stock: producto.stock
      }, 1);
      this.mensajeCarrito = `"${producto.nombre}" agregado al carrito`;
      setTimeout(() => this.mensajeCarrito = '', 2200);
    } catch (e: any) {
      this.mensajeCarrito = e.message;
      setTimeout(() => this.mensajeCarrito = '', 2800);
    }
  }

  scrollCarrusel(carruselId: string, dir: 1 | -1): void {
    const el = document.getElementById(carruselId);
    if (!el) return;
    const cardWidth = 240;
    el.scrollBy({ left: dir * cardWidth * 2, behavior: 'smooth' });
  }

  formatPrecio(precio: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0
    }).format(precio);
  }

  // Categorías destacadas: las 4 con más productos
  get categoriasDestacadas(): CategoriaResponse[] {
    return [...this.categorias]
      .map(c => ({
        cat: c,
        count: this.productos.filter(p => p.categoriaId === c.id).length
      }))
      .sort((a, b) => b.count - a.count)
      .slice(0, 4)
      .map(x => x.cat);
  }
}
