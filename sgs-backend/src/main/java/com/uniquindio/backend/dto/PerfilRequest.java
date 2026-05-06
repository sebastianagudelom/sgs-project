package com.uniquindio.backend.dto;

import jakarta.validation.constraints.*;

public record PerfilRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
        String apellido,

        @NotBlank(message = "La cédula es obligatoria")
        @Pattern(regexp = "^\\d{6,20}$", message = "La cédula debe contener entre 6 y 20 dígitos numéricos")
        String cedula,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^\\d{7,15}$", message = "El teléfono debe contener entre 7 y 15 dígitos numéricos")
        String telefono
) {
}
