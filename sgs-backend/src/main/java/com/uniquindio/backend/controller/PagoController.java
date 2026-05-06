package com.uniquindio.backend.controller;

import com.uniquindio.backend.dto.PedidoRequest;
import com.uniquindio.backend.dto.PreferenciaResponse;
import com.uniquindio.backend.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    /**
     * Crea un pedido y genera la preferencia de Mercado Pago.
     * Retorna la URL de checkout para redirigir al usuario.
     */
    @PostMapping("/crear")
    public ResponseEntity<PreferenciaResponse> crearPreferencia(
            @Valid @RequestBody PedidoRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        PreferenciaResponse response = pagoService.crearPreferencia(email, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Webhook de Mercado Pago (notificación IPN).
     * Recibe notificaciones cuando el estado de un pago cambia.
     */
    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
            @RequestParam(value = "topic", required = false) String topic,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "type", required = false) String type,
            @RequestBody(required = false) Map<String, Object> body) {

        log.info("Webhook recibido - topic: {}, id: {}, type: {}, body: {}", topic, id, type, body);

        // Formato IPN clásico: ?topic=payment&id=12345
        if (topic != null && id != null) {
            pagoService.procesarWebhook(topic, id);
            return ResponseEntity.ok().build();
        }

        // Formato webhook v2: body con { type, data: { id } }
        if (body != null && body.containsKey("type") && body.containsKey("data")) {
            String bodyType = (String) body.get("type");
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            if (data != null && data.containsKey("id")) {
                String dataId = String.valueOf(data.get("id"));
                pagoService.procesarWebhookV2(bodyType, dataId);
            }
            return ResponseEntity.ok().build();
        }

        log.warn("Webhook con formato no reconocido");
        return ResponseEntity.ok().build();
    }
}
