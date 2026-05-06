package com.uniquindio.backend.dto;

import java.math.BigDecimal;

public record DetallePedidoResponse(
        Long id,
        Long productoId,
        String productoNombre,
        String productoImagenUrl,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
