export interface AlertaInventarioResponse {
  id: number;
  productoId: number;
  productoNombre: string;
  productoImagenUrl: string;
  stockActual: number;
  umbral: number;
  estado: 'ACTIVA' | 'LEIDA' | 'RESUELTA';
  fechaCreacion: string;
  fechaActualizacion: string | null;
}
