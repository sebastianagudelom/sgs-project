package com.uniquindio.backend.dto;

import jakarta.validation.constraints.*;

public record RegistroRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
        String apellido,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe proporcionar un email válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
                message = "La contraseña debe contener al menos una mayúscula, una minúscula y un número"
        )
        String password,

        @NotBlank(message = "La cédula es obligatoria")
        @Pattern(regexp = "^[0-9]{6,20}$", message = "La cédula debe tener entre 6 y 20 dígitos")
        String cedula,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^[0-9]{7,15}$", message = "El teléfono debe tener entre 7 y 15 dígitos")
        String telefono
) {
}
