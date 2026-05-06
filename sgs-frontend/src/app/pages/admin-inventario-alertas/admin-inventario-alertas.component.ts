import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AdminService } from '../../services/admin.service';
import { AlertaInventarioResponse } from '../../models/inventario-alerta.model';

@Component({
  selector: 'app-admin-inventario-alertas',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-inventario-alertas.component.html',
  styleUrl: './admin-inventario-alertas.component.css'
})
export class AdminInventarioAlertasComponent implements OnInit {
  alertas: AlertaInventarioResponse[] = [];
  loading = true;
  checking = false;
  errorMessage = '';
  successMessage = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.cargarAlertas();
  }

  cargarAlertas(): void {
    this.loading = true;
    this.errorMessage = '';

    this.adminService.listarAlertasInventario().subscribe({
      next: (alertas) => {
        this.alertas = alertas;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.mensaje || 'Error al cargar las alertas';
        this.loading = false;
      }
    });
  }

  verificarInventario(): void {
    this.checking = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.adminService.verificarInventario().subscribe({
      next: (alertas) => {
        this.alertas = alertas;
        this.successMessage = 'Inventario verificado';
        this.checking = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.mensaje || 'Error al verificar inventario';
        this.checking = false;
      }
    });
  }

  marcarLeida(alerta: AlertaInventarioResponse): void {
    this.adminService.marcarAlertaInventarioLeida(alerta.id).subscribe({
      next: (actualizada) => {
        const idx = this.alertas.findIndex(item => item.id === alerta.id);
        if (idx >= 0) {
          this.alertas[idx] = actualizada;
        }
      },
      error: (err) => {
        this.errorMessage = err.error?.mensaje || 'Error al actualizar la alerta';
      }
    });
  }

  get activas(): number {
    return this.alertas.filter(alerta => alerta.estado === 'ACTIVA').length;
  }

  get leidas(): number {
    return this.alertas.filter(alerta => alerta.estado === 'LEIDA').length;
  }

  getEstadoLabel(estado: string): string {
    switch (estado) {
      case 'ACTIVA': return 'Activa';
      case 'LEIDA': return 'Leida';
      case 'RESUELTA': return 'Resuelta';
      default: return estado;
    }
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
