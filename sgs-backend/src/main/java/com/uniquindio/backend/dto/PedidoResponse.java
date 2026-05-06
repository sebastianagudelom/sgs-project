package com.uniquindio.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        String estado,
        BigDecimal total,
        String direccionEnvio,
        String usuarioNombre,
        String usuarioEmail,
        List<DetallePedidoResponse> detalles,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
