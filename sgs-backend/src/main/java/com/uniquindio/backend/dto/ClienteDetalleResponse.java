package com.uniquindio.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ClienteDetalleResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        String cedula,
        String telefono,
        boolean activo,
        long totalPedidos,
        BigDecimal totalGastado,
        LocalDateTime fechaRegistro,
        List<DireccionResponse> direcciones
) {
}
