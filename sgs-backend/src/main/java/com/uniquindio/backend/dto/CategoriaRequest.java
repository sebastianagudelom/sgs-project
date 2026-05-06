package com.uniquindio.backend.dto;

import jakarta.validation.constraints.*;

public record CategoriaRequest(

        @NotBlank(message = "El nombre de la categoría es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String nombre,

        @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
        String descripcion
) {
}
