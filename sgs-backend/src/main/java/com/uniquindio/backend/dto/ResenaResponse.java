package com.uniquindio.backend.dto;

import java.time.LocalDateTime;

public record ResenaResponse(
        Long id,
        Long productoId,
        Long usuarioId,
        String usuarioNombre,
        Integer calificacion,
        String comentario,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
