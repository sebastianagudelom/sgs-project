package com.uniquindio.backend.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record DireccionRequest(

        @NotBlank(message = "El nombre de la dirección es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String nombre,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(min = 10, max = 500, message = "La dirección debe tener entre 10 y 500 caracteres")
        String direccion,

        @DecimalMin(value = "-90.0", message = "La latitud debe ser mayor o igual a -90")
        @DecimalMax(value = "90.0", message = "La latitud debe ser menor o igual a 90")
        BigDecimal latitud,

        @DecimalMin(value = "-180.0", message = "La longitud debe ser mayor o igual a -180")
        @DecimalMax(value = "180.0", message = "La longitud debe ser menor o igual a 180")
        BigDecimal longitud,

        boolean predeterminada
) {
}
