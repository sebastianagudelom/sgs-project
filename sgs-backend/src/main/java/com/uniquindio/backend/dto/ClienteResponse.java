package com.uniquindio.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        String cedula,
        String telefono,
        boolean activo,
        long totalPedidos,
        BigDecimal totalGastado,
        LocalDateTime fechaRegistro
) {}
