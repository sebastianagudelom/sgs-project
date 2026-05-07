package com.uniquindio.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteAdminRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
        String apellido,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no es válido")
        @Size(max = 100, message = "El email no puede exceder 100 caracteres")
        String email,

        @Size(max = 20, message = "La cédula no puede exceder 20 caracteres")
        String cedula,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String telefono,

        // Solo se usa al crear; al actualizar se ignora
        String password
) {
}
