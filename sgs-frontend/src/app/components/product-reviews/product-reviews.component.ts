import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { ResenaService } from '../../services/resena.service';
import {
  MiEstadoResenaResponse,
  ResenaResponse,
  ResenaResumenResponse
} from '../../models/resena.model';

@Component({
  selector: 'app-product-reviews',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-reviews.component.html',
  styleUrl: './product-reviews.component.css'
})
export class ProductReviewsComponent implements OnInit, OnChanges {
  @Input({ required: true }) productoId!: number;

  resenas: ResenaResponse[] = [];
  resumen: ResenaResumenResponse = { promedio: 0, total: 0 };
  miEstado: MiEstadoResenaResponse | null = null;
  loading = true;
  saving = false;
  errorMessage = '';
  successMessage = '';
  calificacion = 0;
  hoverCalificacion = 0;
  comentario = '';
  stars = [1, 2, 3, 4, 5];

  constructor(
    public authService: AuthService,
    private resenaService: ResenaService
  ) {}

  ngOnInit(): void {
    this.cargarResenas();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['productoId'] && !changes['productoId'].firstChange) {
      this.cargarResenas();
    }
  }

  cargarResenas(): void {
    if (!this.productoId) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const estado$ = this.authService.isLoggedIn() && !this.authService.isAdmin()
      ? this.resenaService.obtenerMiEstado(this.productoId)
      : of(null);

    forkJoin({
      resenas: this.resenaService.listarPorProducto(this.productoId),
      resumen: this.resenaService.obtenerResumen(this.productoId),
      estado: estado$
    }).subscribe({
      next: ({ resenas, resumen, estado }) => {
        this.resenas = resenas;
        this.resumen = resumen;
        this.miEstado = estado;
        this.cargarFormularioPropio();
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.mensaje || 'Error al cargar las resenas';
        this.loading = false;
      }
    });
  }

  seleccionarCalificacion(valor: number): void {
    this.calificacion = valor;
  }

  guardarResena(): void {
    if (!this.calificacion || this.comentario.trim().length < 3) {
      this.errorMessage = 'Selecciona una calificacion y escribe un comentario';
      return;
    }

    this.saving = true;
    this.errorMessage = '';
    this.successMessage = '';

    const request = {
      calificacion: this.calificacion,
      comentario: this.comentario.trim()
    };

    const obs = this.miEstado?.miResena
      ? this.resenaService.actualizar(this.miEstado.miResena.id, request)
      : this.resenaService.crear(this.productoId, request);

    obs.subscribe({
      next: () => {
        this.saving = false;
        this.successMessage = this.miEstado?.miResena ? 'Resena actualizada' : 'Resena publicada';
        this.cargarResenas();
      },
      error: (err) => {
        this.saving = false;
        this.errorMessage = err.error?.mensaje || 'No fue posible guardar la resena';
      }
    });
  }

  puedeMostrarFormulario(): boolean {
    return !!this.miEstado && (this.miEstado.puedeResenar || !!this.miEstado.miResena);
  }

  mensajeRestriccion(): string {
    if (!this.authService.isLoggedIn()) {
      return 'Inicia sesion para escribir una resena.';
    }

    if (this.authService.isAdmin()) {
      return 'Los administradores pueden consultar las resenas, pero no califican productos.';
    }

    if (this.miEstado && !this.miEstado.compraVerificada) {
      return 'Solo puedes calificar productos que ya compraste.';
    }

    return '';
  }

  formatFecha(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-CO', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }

  private cargarFormularioPropio(): void {
    if (this.miEstado?.miResena) {
      this.calificacion = this.miEstado.miResena.calificacion;
      this.comentario = this.miEstado.miResena.comentario;
      return;
    }

    this.calificacion = 0;
    this.comentario = '';
  }
}
