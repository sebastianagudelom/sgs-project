import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ProductoRequest, ProductoResponse } from '../models/producto.model';

@Injectable({ providedIn: 'root' })
export class ProductoService {

  private apiUrl = `${environment.apiUrl}/productos`;
  private imagenUrl = `${environment.apiUrl}/imagenes`;

  constructor(private http: HttpClient) {}

  listarActivos(): Observable<ProductoResponse[]> {
    return this.http.get<ProductoResponse[]>(this.apiUrl);
  }

  listarTodos(): Observable<ProductoResponse[]> {
    return this.http.get<ProductoResponse[]>(`${this.apiUrl}/todos`);
  }

  obtenerPorId(id: number): Observable<ProductoResponse> {
    return this.http.get<ProductoResponse>(`${this.apiUrl}/${id}`);
  }

  buscarPorNombre(nombre: string): Observable<ProductoResponse[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<ProductoResponse[]>(`${this.apiUrl}/buscar`, { params });
  }

  listarPorCategoria(categoriaId: number): Observable<ProductoResponse[]> {
    return this.http.get<ProductoResponse[]>(`${this.apiUrl}/categoria/${categoriaId}`);
  }

  crear(request: ProductoRequest): Observable<ProductoResponse> {
    return this.http.post<ProductoResponse>(this.apiUrl, request);
  }

  actualizar(id: number, request: ProductoRequest): Observable<ProductoResponse> {
    return this.http.put<ProductoResponse>(`${this.apiUrl}/${id}`, request);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  toggleActivo(id: number): Observable<ProductoResponse> {
    return this.http.patch<ProductoResponse>(`${this.apiUrl}/${id}/toggle-activo`, {});
  }

  subirImagen(archivo: File): Observable<{ url: string }> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    return this.http.post<{ url: string }>(`${this.imagenUrl}/upload`, formData);
  }
}
