package com.uniquindio.backend.dto;

public record AuthResponse(
        String token,
        String email,
        String nombre,
        String rol
) {
}
