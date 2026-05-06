package com.uniquindio.backend.dto;

import jakarta.validation.constraints.*;

public record LoginRequest(

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe proporcionar un email válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}
