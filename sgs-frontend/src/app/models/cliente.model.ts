import { DireccionResponse } from './perfil.model';

export interface ClienteResponse {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  cedula: string;
  telefono: string;
  activo: boolean;
  totalPedidos: number;
  totalGastado: number;
  fechaRegistro: string;
}

export interface ClienteDetalleResponse extends ClienteResponse {
  direcciones: DireccionResponse[];
}

export interface ClienteAdminRequest {
  nombre: string;
  apellido: string;
  email: string;
  cedula: string;
  telefono: string;
  password?: string;
}

export interface ResetPasswordRequest {
  nuevaPassword: string;
}
