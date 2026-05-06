package com.uniquindio.backend.dto;

import jakarta.validation.constraints.*;

public record ResenaRequest(

        @NotNull(message = "La calificacion es obligatoria")
        @Min(value = 1, message = "La calificacion minima es 1")
        @Max(value = 5, message = "La calificacion maxima es 5")
        Integer calificacion,

        @NotBlank(message = "El comentario es obligatorio")
        @Size(min = 3, max = 1000, message = "El comentario debe tener entre 3 y 1000 caracteres")
        String comentario
) {
}
