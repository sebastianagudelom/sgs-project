package com.uniquindio.backend.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductoRequest(

        @NotBlank(message = "El nombre del producto es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        @Min(value = 1, message = "El umbral de stock debe ser mayor a 0")
        Integer stockMinimo,

        @Size(max = 500)
        String imagenUrl,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId
) {
}
