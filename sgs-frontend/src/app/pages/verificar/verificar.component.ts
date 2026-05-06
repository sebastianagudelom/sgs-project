import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-verificar',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './verificar.component.html',
  styleUrl: './verificar.component.css'
})
export class VerificarComponent implements OnInit {
  verificarForm: FormGroup;
  email = '';
  errorMessage = '';
  successMessage = '';
  loading = false;
  resending = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.verificarForm = this.fb.group({
      codigo: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]]
    });
  }

  ngOnInit(): void {
    this.email = this.route.snapshot.queryParams['email'] || '';
    if (!this.email) {
      this.router.navigate(['/registro']);
    }
  }

  onSubmit(): void {
    if (this.verificarForm.invalid) {
      this.verificarForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.verificar({
      email: this.email,
      codigo: this.verificarForm.value.codigo
    }).subscribe({
      next: (res) => {
        this.successMessage = res.mensaje;
        this.loading = false;
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.mensaje || 'Código incorrecto';
      }
    });
  }

  reenviarCodigo(): void {
    this.resending = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.reenviarCodigo(this.email).subscribe({
      next: (res) => {
        this.resending = false;
        this.successMessage = res.mensaje;
      },
      error: (err) => {
        this.resending = false;
        this.errorMessage = err.error?.mensaje || 'Error al reenviar código';
      }
    });
  }

  get f() {
    return this.verificarForm.controls;
  }
}
