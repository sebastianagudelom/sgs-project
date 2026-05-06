export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegistroRequest {
  nombre: string;
  apellido: string;
  email: string;
  password: string;
  cedula: string;
  telefono: string;
}

export interface VerificacionRequest {
  email: string;
  codigo: string;
}

export interface AuthResponse {
  token: string;
  email: string;
  nombre: string;
  rol: string;
}

export interface MensajeResponse {
  mensaje: string;
}
