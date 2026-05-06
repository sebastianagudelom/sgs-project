import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  PerfilRequest,
  PerfilResponse,
  DireccionRequest,
  DireccionResponse
} from '../models/perfil.model';

@Injectable({ providedIn: 'root' })
export class PerfilService {

  private apiUrl = `${environment.apiUrl}/perfil`;

  constructor(private http: HttpClient) {}

  obtenerPerfil(): Observable<PerfilResponse> {
    return this.http.get<PerfilResponse>(this.apiUrl);
  }

  actualizarPerfil(request: PerfilRequest): Observable<PerfilResponse> {
    return this.http.put<PerfilResponse>(this.apiUrl, request);
  }

  listarDirecciones(): Observable<DireccionResponse[]> {
    return this.http.get<DireccionResponse[]>(`${this.apiUrl}/direcciones`);
  }

  crearDireccion(request: DireccionRequest): Observable<DireccionResponse> {
    return this.http.post<DireccionResponse>(`${this.apiUrl}/direcciones`, request);
  }

  actualizarDireccion(id: number, request: DireccionRequest): Observable<DireccionResponse> {
    return this.http.put<DireccionResponse>(`${this.apiUrl}/direcciones/${id}`, request);
  }

  eliminarDireccion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/direcciones/${id}`);
  }

  marcarPredeterminada(id: number): Observable<DireccionResponse> {
    return this.http.patch<DireccionResponse>(`${this.apiUrl}/direcciones/${id}/predeterminada`, {});
  }
}
