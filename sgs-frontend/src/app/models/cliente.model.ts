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
