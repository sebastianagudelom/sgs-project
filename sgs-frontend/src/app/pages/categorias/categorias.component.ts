import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoriaService } from '../../services/categoria.service';
import { CategoriaResponse } from '../../models/categoria.model';

@Component({
  selector: 'app-categorias',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './categorias.component.html',
  styleUrl: './categorias.component.css'
})
export class CategoriasComponent implements OnInit {
  categorias: CategoriaResponse[] = [];
  categoriaForm: FormGroup;
  editandoId: number | null = null;
  mostrarForm = false;
  loading = true;
  errorMessage = '';
  saving = false;

  constructor(
    private categoriaService: CategoriaService,
    private fb: FormBuilder
  ) {
    this.categoriaForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      descripcion: ['', [Validators.maxLength(200)]]
    });
  }

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.loading = true;
    this.categoriaService.listarTodas().subscribe({
      next: (data) => {
        this.categorias = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  abrirFormNuevo(): void {
    this.editandoId = null;
    this.categoriaForm.reset();
    this.mostrarForm = true;
    this.errorMessage = '';
  }

  editarCategoria(cat: CategoriaResponse): void {
    this.editandoId = cat.id;
    this.categoriaForm.patchValue({
      nombre: cat.nombre,
      descripcion: cat.descripcion
    });
    this.mostrarForm = true;
    this.errorMessage = '';
  }

  cancelar(): void {
    this.mostrarForm = false;
    this.editandoId = null;
    this.categoriaForm.reset();
    this.errorMessage = '';
  }

  guardar(): void {
    if (this.categoriaForm.invalid) {
      this.categoriaForm.markAllAsTouched();
      return;
    }

    this.saving = true;
    this.errorMessage = '';

    const formValue = this.categoriaForm.value;

    const obs = this.editandoId
      ? this.categoriaService.actualizar(this.editandoId, formValue)
      : this.categoriaService.crear(formValue);

    obs.subscribe({
      next: () => {
        this.saving = false;
        this.cancelar();
        this.cargarCategorias();
      },
      error: (err) => {
        this.saving = false;
        this.errorMessage = err.error?.mensaje || 'Error al guardar la categoría';
      }
    });
  }

  eliminarCategoria(id: number): void {
    if (confirm('¿Estás seguro de eliminar esta categoría?')) {
      this.categoriaService.eliminar(id).subscribe({
        next: () => this.cargarCategorias(),
        error: (err) => {
          this.errorMessage = err.error?.mensaje || 'No se puede eliminar la categoría';
        }
      });
    }
  }

  get f() {
    return this.categoriaForm.controls;
  }

  private categoryColors = ['#00a650', '#ff9800', '#e53935', '#1565c0', '#7b1fa2', '#00897b', '#f4511e', '#6d4c41', '#546e7a', '#d81b60'];

  getCategoryColor(index: number): string {
    return this.categoryColors[index % this.categoryColors.length];
  }

  getCategoryEmoji(nombre: string): string {
    return nombre.charAt(0).toUpperCase();
  }
}
