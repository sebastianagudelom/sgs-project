import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ClienteResponse } from '../models/cliente.model';
import { AlertaInventarioResponse } from '../models/inventario-alerta.model';

@Injectable({ providedIn: 'root' })
export class AdminService {

  private apiUrl = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient) {}

  listarClientes(): Observable<ClienteResponse[]> {
    return this.http.get<ClienteResponse[]>(`${this.apiUrl}/clientes`);
  }

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
