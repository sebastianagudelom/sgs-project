import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { ClienteResponse } from '../../models/cliente.model';

@Component({
  selector: 'app-admin-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-clientes.component.html',
  styleUrl: './admin-clientes.component.css'
})
export class AdminClientesComponent implements OnInit {
  clientes: ClienteResponse[] = [];
  clientesFiltrados: ClienteResponse[] = [];
  loading = true;
  busqueda = '';
  filtroActividad = 'todos';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.loading = true;
    this.adminService.listarClientes().subscribe({
      next: (data) => {
        this.clientes = data;
        this.filtrar();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  filtrar(): void {
    let resultado = this.clientes;

    if (this.filtroActividad === 'con-pedidos') {
      resultado = resultado.filter(c => c.totalPedidos > 0);
    } else if (this.filtroActividad === 'sin-pedidos') {
      resultado = resultado.filter(c => c.totalPedidos === 0);
    }

    if (this.busqueda.trim()) {
      const term = this.busqueda.trim().toLowerCase();
      resultado = resultado.filter(c =>
        c.nombre.toLowerCase().includes(term) ||
        c.apellido.toLowerCase().includes(term) ||
        c.email.toLowerCase().includes(term) ||
        (c.cedula && c.cedula.includes(term))
      );
    }

    this.clientesFiltrados = resultado;
  }

  getTotalPedidos(): number {
    return this.clientesFiltrados.reduce((sum, c) => sum + c.totalPedidos, 0);
  }

  getTotalGastado(): number {
    return this.clientesFiltrados.reduce((sum, c) => sum + c.totalGastado, 0);
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
      month: 'short',
      day: 'numeric'
    });
  }
}
