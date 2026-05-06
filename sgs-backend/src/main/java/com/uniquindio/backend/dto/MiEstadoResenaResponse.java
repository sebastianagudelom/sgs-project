package com.uniquindio.backend.dto;

public record MiEstadoResenaResponse(
        boolean compraVerificada,
        boolean yaReseno,
        boolean puedeResenar,
        ResenaResponse miResena
) {
}
