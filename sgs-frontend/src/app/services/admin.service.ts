import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  ClienteResponse,
  ClienteDetalleResponse,
  ClienteAdminRequest,
  ResetPasswordRequest
} from '../models/cliente.model';
import { DireccionRequest, DireccionResponse } from '../models/perfil.model';
import { AlertaInventarioResponse } from '../models/inventario-alerta.model';

@Injectable({ providedIn: 'root' })
export class AdminService {

  private apiUrl = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient) {}

  // ===== Clientes =====

  listarClientes(): Observable<ClienteResponse[]> {
    return this.http.get<ClienteResponse[]>(`${this.apiUrl}/clientes`);
  }

  obtenerCliente(id: number): Observable<ClienteDetalleResponse> {
    return this.http.get<ClienteDetalleResponse>(`${this.apiUrl}/clientes/${id}`);
  }

  crearCliente(req: ClienteAdminRequest): Observable<ClienteResponse> {
    return this.http.post<ClienteResponse>(`${this.apiUrl}/clientes`, req);
  }

  actualizarCliente(id: number, req: ClienteAdminRequest): Observable<ClienteResponse> {
    return this.http.put<ClienteResponse>(`${this.apiUrl}/clientes/${id}`, req);
  }

  cambiarEstadoCliente(id: number): Observable<ClienteResponse> {
    return this.http.patch<ClienteResponse>(`${this.apiUrl}/clientes/${id}/estado`, {});
  }

  resetPasswordCliente(id: number, req: ResetPasswordRequest): Observable<{ mensaje: string }> {
    return this.http.patch<{ mensaje: string }>(`${this.apiUrl}/clientes/${id}/password`, req);
  }

  // ===== Direcciones del cliente =====

  listarDireccionesCliente(clienteId: number): Observable<DireccionResponse[]> {
    return this.http.get<DireccionResponse[]>(`${this.apiUrl}/clientes/${clienteId}/direcciones`);
  }

  crearDireccionCliente(clienteId: number, req: DireccionRequest): Observable<DireccionResponse> {
    return this.http.post<DireccionResponse>(`${this.apiUrl}/clientes/${clienteId}/direcciones`, req);
  }

  actualizarDireccionCliente(clienteId: number, direccionId: number, req: DireccionRequest): Observable<DireccionResponse> {
    return this.http.put<DireccionResponse>(`${this.apiUrl}/clientes/${clienteId}/direcciones/${direccionId}`, req);
  }

  eliminarDireccionCliente(clienteId: number, direccionId: number): Observable<{ mensaje: string }> {
    return this.http.delete<{ mensaje: string }>(`${this.apiUrl}/clientes/${clienteId}/direcciones/${direccionId}`);
  }

  // ===== Inventario =====

  listarAlertasInventario(): Observable<AlertaInventarioResponse[]> {
    return this.http.get<AlertaInventarioResponse[]>(`${this.apiUrl}/inventario/alertas`);
  }

  verificarInventario(): Observable<AlertaInventarioResponse[]> {
    return this.http.post<AlertaInventarioResponse[]>(`${this.apiUrl}/inventario/verificar`, {});
  }

  contarAlertasInventario(): Observable<{ total: number }> {
    return this.http.get<{ total: number }>(`${this.apiUrl}/inventario/alertas/contador`);
  }

  marcarAlertaInventarioLeida(id: number): Observable<AlertaInventarioResponse> {
    return this.http.patch<AlertaInventarioResponse>(`${this.apiUrl}/inventario/alertas/${id}/leida`, {});
  }
}
