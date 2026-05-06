package com.uniquindio.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record FacturaResponse(
        Long pedidoId,
        String clienteNombre,
        String clienteEmail,
        String clienteCedula,
        String clienteTelefono,
        String direccionEnvio,
        String estado,
        String mercadoPagoPaymentId,
        BigDecimal total,
        List<DetallePedidoResponse> detalles,
        LocalDateTime fechaPago,
        LocalDateTime fechaCreacion
) {
}
