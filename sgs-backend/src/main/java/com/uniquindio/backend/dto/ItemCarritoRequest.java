package com.uniquindio.backend.dto;

import jakarta.validation.constraints.*;

public record ItemCarritoRequest(

        @NotNull(message = "El producto es obligatorio")
        Long productoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad mínima es 1")
        Integer cantidad
) {
}
