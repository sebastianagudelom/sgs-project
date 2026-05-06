package com.uniquindio.backend.dto;

import jakarta.validation.constraints.*;

public record VerificacionRequest(

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe proporcionar un email válido")
        String email,

        @NotBlank(message = "El código de verificación es obligatorio")
        @Size(min = 6, max = 6, message = "El código debe tener 6 dígitos")
        String codigo
) {
}
