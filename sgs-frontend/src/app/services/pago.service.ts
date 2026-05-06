import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PedidoRequest } from '../models/pedido.model';

export interface PreferenciaResponse {
  pedidoId: number;
  initPoint: string;
}

@Injectable({ providedIn: 'root' })
export class PagoService {

  private apiUrl = `${environment.apiUrl}/pagos`;

  constructor(private http: HttpClient) {}

  crearPreferencia(request: PedidoRequest): Observable<PreferenciaResponse> {
    return this.http.post<PreferenciaResponse>(`${this.apiUrl}/crear`, request);
  }
}
