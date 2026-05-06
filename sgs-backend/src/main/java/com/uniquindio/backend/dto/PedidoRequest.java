package com.uniquindio.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record PedidoRequest(

        @NotEmpty(message = "El pedido debe tener al menos un producto")
        @Valid
        List<ItemCarritoRequest> items,

        @NotBlank(message = "La dirección de envío es obligatoria")
        @Size(min = 10, max = 500, message = "La dirección debe tener entre 10 y 500 caracteres")
        String direccionEnvio
) {
}
