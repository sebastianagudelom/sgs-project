import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  MiEstadoResenaResponse,
  ResenaRequest,
  ResenaResponse,
  ResenaResumenResponse
} from '../models/resena.model';

@Injectable({ providedIn: 'root' })
export class ResenaService {

  private apiUrl = `${environment.apiUrl}/resenas`;

  constructor(private http: HttpClient) {}

  listarPorProducto(productoId: number): Observable<ResenaResponse[]> {
    return this.http.get<ResenaResponse[]>(`${this.apiUrl}/producto/${productoId}`);
  }

  obtenerResumen(productoId: number): Observable<ResenaResumenResponse> {
    return this.http.get<ResenaResumenResponse>(`${this.apiUrl}/producto/${productoId}/resumen`);
  }

  obtenerMiEstado(productoId: number): Observable<MiEstadoResenaResponse> {
    return this.http.get<MiEstadoResenaResponse>(`${this.apiUrl}/producto/${productoId}/mi-estado`);
  }

  crear(productoId: number, request: ResenaRequest): Observable<ResenaResponse> {
    return this.http.post<ResenaResponse>(`${this.apiUrl}/producto/${productoId}`, request);
  }

  actualizar(resenaId: number, request: ResenaRequest): Observable<ResenaResponse> {
    return this.http.put<ResenaResponse>(`${this.apiUrl}/${resenaId}`, request);
  }

  eliminar(resenaId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${resenaId}`);
  }
}
