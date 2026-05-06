package com.uniquindio.backend.dto;

import java.time.LocalDateTime;

public record AlertaInventarioResponse(
        Long id,
        Long productoId,
        String productoNombre,
        String productoImagenUrl,
        Integer stockActual,
        Integer umbral,
        String estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
