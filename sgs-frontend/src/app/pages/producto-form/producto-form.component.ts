import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { CategoriaService } from '../../services/categoria.service';
import { CategoriaResponse } from '../../models/categoria.model';

@Component({
  selector: 'app-producto-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './producto-form.component.html',
  styleUrl: './producto-form.component.css'
})
export class ProductoFormComponent implements OnInit {
  productoForm: FormGroup;
  categorias: CategoriaResponse[] = [];
  editando = false;
  productoId: number | null = null;
  errorMessage = '';
  loading = false;
  archivoSeleccionado: File | null = null;
  imagenPreview: string | null = null;
  subiendoImagen = false;

  constructor(
    private fb: FormBuilder,
    private productoService: ProductoService,
    private categoriaService: CategoriaService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.productoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      descripcion: ['', [Validators.maxLength(500)]],
      precio: [null, [Validators.required, Validators.min(1)]],
      stock: [null, [Validators.required, Validators.min(0)]],
      stockMinimo: [5, [Validators.required, Validators.min(1)]],
      imagenUrl: [''],
      categoriaId: [null, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.cargarCategorias();

    const id = this.route.snapshot.params['id'];
    if (id) {
      this.editando = true;
      this.productoId = +id;
      this.cargarProducto(this.productoId);
    }
  }

  cargarCategorias(): void {
    this.categoriaService.listarTodas().subscribe({
      next: (data) => this.categorias = data
    });
  }

  cargarProducto(id: number): void {
    this.productoService.obtenerPorId(id).subscribe({
      next: (producto) => {
        this.productoForm.patchValue({
          nombre: producto.nombre,
          descripcion: producto.descripcion,
          precio: producto.precio,
          stock: producto.stock,
          stockMinimo: producto.stockMinimo || 5,
          imagenUrl: producto.imagenUrl,
          categoriaId: producto.categoriaId
        });
        if (producto.imagenUrl) {
          this.imagenPreview = producto.imagenUrl;
        }
      },
      error: () => {
        this.router.navigate(['/productos']);
      }
    });
  }

  onArchivoSeleccionado(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.archivoSeleccionado = input.files[0];

      // Preview local
      const reader = new FileReader();
      reader.onload = () => {
        this.imagenPreview = reader.result as string;
      };
      reader.readAsDataURL(this.archivoSeleccionado);
    }
  }

  eliminarImagen(): void {
    this.archivoSeleccionado = null;
    this.imagenPreview = null;
    this.productoForm.patchValue({ imagenUrl: '' });
  }

  onSubmit(): void {
    if (this.productoForm.invalid) {
      this.productoForm.markAllAsTouched();
      return;
    }

    // Validar imagen obligatoria (nuevo producto debe tener archivo, editando debe tener preview o archivo)
    if (!this.archivoSeleccionado && !this.imagenPreview) {
      this.errorMessage = 'La imagen del producto es obligatoria';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    if (this.archivoSeleccionado) {
      // Primero subir imagen, luego guardar producto
      this.subiendoImagen = true;
      this.productoService.subirImagen(this.archivoSeleccionado).subscribe({
        next: (res) => {
          this.subiendoImagen = false;
          this.productoForm.patchValue({ imagenUrl: res.url });
          this.guardarProducto();
        },
        error: (err) => {
          this.loading = false;
          this.subiendoImagen = false;
          this.errorMessage = err.error?.mensaje || 'Error al subir la imagen';
        }
      });
    } else {
      this.guardarProducto();
    }
  }

  private guardarProducto(): void {
    const formValue = this.productoForm.value;

    const obs = this.editando && this.productoId
      ? this.productoService.actualizar(this.productoId, formValue)
      : this.productoService.crear(formValue);

    obs.subscribe({
      next: () => {
        this.router.navigate(['/productos']);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.mensaje || 'Error al guardar el producto';
      }
    });
  }

  get f() {
    return this.productoForm.controls;
  }
}
