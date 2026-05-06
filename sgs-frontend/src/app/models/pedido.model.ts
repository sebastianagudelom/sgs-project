export interface ItemCarrito {
  productoId: number;
  nombre: string;
  precio: number;
  cantidad: number;
  imagenUrl: string;
  stock: number;
}

export interface PedidoRequest {
  items: { productoId: number; cantidad: number }[];
  direccionEnvio: string;
}

export interface DetallePedidoResponse {
  id: number;
  productoId: number;
  productoNombre: string;
  productoImagenUrl: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface PedidoResponse {
  id: number;
  estado: string;
  total: number;
  direccionEnvio: string;
  usuarioNombre: string;
  usuarioEmail: string;
  detalles: DetallePedidoResponse[];
  fechaCreacion: string;
  fechaActualizacion: string;
}

export interface FacturaResponse {
  pedidoId: number;
  clienteNombre: string;
  clienteEmail: string;
  clienteCedula: string;
  clienteTelefono: string;
  direccionEnvio: string;
  estado: string;
  mercadoPagoPaymentId: string;
  total: number;
  detalles: DetallePedidoResponse[];
  fechaPago: string;
  fechaCreacion: string;
}
