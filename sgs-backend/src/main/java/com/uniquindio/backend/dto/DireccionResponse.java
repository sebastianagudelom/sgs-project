package com.uniquindio.backend.dto;

import java.math.BigDecimal;

public record DireccionResponse(
        Long id,
        String nombre,
        String direccion,
        BigDecimal latitud,
        BigDecimal longitud,
        boolean predeterminada
) {
}
