package com.uniquindio.backend.dto;

import java.util.List;

public record PerfilResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        String cedula,
        String telefono,
        List<DireccionResponse> direcciones
) {
}
