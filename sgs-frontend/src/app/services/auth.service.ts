import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  LoginRequest,
  RegistroRequest,
  VerificacionRequest,
  AuthResponse,
  MensajeResponse
} from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<AuthResponse | null>(this.getStoredUser());

  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  registro(request: RegistroRequest): Observable<MensajeResponse> {
    return this.http.post<MensajeResponse>(`${this.apiUrl}/registro`, request);
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(response));
        this.currentUserSubject.next(response);
      })
    );
  }

  verificar(request: VerificacionRequest): Observable<MensajeResponse> {
    return this.http.post<MensajeResponse>(`${this.apiUrl}/verificar`, request);
  }

  reenviarCodigo(email: string): Observable<MensajeResponse> {
    const params = new HttpParams().set('email', email);
    return this.http.post<MensajeResponse>(`${this.apiUrl}/reenviar-codigo`, null, { params });
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    const user = this.currentUserSubject.value;
    return user?.rol === 'ADMIN';
  }

  getCurrentUser(): AuthResponse | null {
    return this.currentUserSubject.value;
  }

  private getStoredUser(): AuthResponse | null {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }
}
