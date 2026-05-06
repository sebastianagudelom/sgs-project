export interface PerfilRequest {
  nombre: string;
  apellido: string;
  cedula: string;
  telefono: string;
}

export interface PerfilResponse {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  cedula: string;
  telefono: string;
  direcciones: DireccionResponse[];
}

export interface DireccionRequest {
  nombre: string;
  direccion: string;
  latitud: number | null;
  longitud: number | null;
  predeterminada: boolean;
}

export interface DireccionResponse {
  id: number;
  nombre: string;
  direccion: string;
  latitud: number | null;
  longitud: number | null;
  predeterminada: boolean;
}
