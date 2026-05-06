import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PedidoRequest, PedidoResponse, FacturaResponse } from '../models/pedido.model';

@Injectable({ providedIn: 'root' })
export class PedidoService {

  private apiUrl = `${environment.apiUrl}/pedidos`;

  constructor(private http: HttpClient) {}

  crearPedido(request: PedidoRequest): Observable<PedidoResponse> {
    return this.http.post<PedidoResponse>(this.apiUrl, request);
  }

  misPedidos(): Observable<PedidoResponse[]> {
    return this.http.get<PedidoResponse[]>(`${this.apiUrl}/mis-pedidos`);
  }

  listarTodos(): Observable<PedidoResponse[]> {
    return this.http.get<PedidoResponse[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<PedidoResponse> {
    return this.http.get<PedidoResponse>(`${this.apiUrl}/${id}`);
  }

  actualizarEstado(id: number, estado: string): Observable<PedidoResponse> {
    const params = new HttpParams().set('estado', estado);
    return this.http.patch<PedidoResponse>(`${this.apiUrl}/${id}/estado`, null, { params });
  }

  cancelarPedido(id: number): Observable<PedidoResponse> {
    return this.http.post<PedidoResponse>(`${this.apiUrl}/${id}/cancelar`, null);
  }

  obtenerFactura(id: number): Observable<FacturaResponse> {
    return this.http.get<FacturaResponse>(`${this.apiUrl}/${id}/factura`);
  }
}
