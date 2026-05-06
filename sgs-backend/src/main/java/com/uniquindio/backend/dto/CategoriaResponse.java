package com.uniquindio.backend.dto;

public record CategoriaResponse(
        Long id,
        String nombre,
        String descripcion,
        int cantidadProductos
) {
}
