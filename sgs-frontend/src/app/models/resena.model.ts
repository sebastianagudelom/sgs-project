export interface ResenaRequest {
  calificacion: number;
  comentario: string;
}

export interface ResenaResponse {
  id: number;
  productoId: number;
  usuarioId: number;
  usuarioNombre: string;
  calificacion: number;
  comentario: string;
  fechaCreacion: string;
  fechaActualizacion: string | null;
}

export interface ResenaResumenResponse {
  promedio: number;
  total: number;
}

export interface MiEstadoResenaResponse {
  compraVerificada: boolean;
  yaReseno: boolean;
  puedeResenar: boolean;
  miResena: ResenaResponse | null;
}
