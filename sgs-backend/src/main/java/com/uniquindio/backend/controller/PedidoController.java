package com.uniquindio.backend.controller;

import com.uniquindio.backend.dto.FacturaResponse;
import com.uniquindio.backend.dto.PedidoRequest;
import com.uniquindio.backend.dto.PedidoResponse;
import com.uniquindio.backend.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    /**
     * Cliente crea un pedido desde su carrito.
     */
    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(
            @Valid @RequestBody PedidoRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(pedidoService.crearPedido(email, request));
    }

    /**
     * Cliente obtiene su historial de pedidos.
     */
    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResponse>> misPedidos(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(pedidoService.listarMisPedidos(email));
    }

    /**
     * Admin obtiene todos los pedidos.
     */
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    /**
     * Obtener un pedido por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    /**
     * Admin actualiza el estado de un pedido.
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }

    /**
     * Cliente cancela su propio pedido (solo si está PENDIENTE).
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelarPedido(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(pedidoService.cancelarPedidoUsuario(email, id));
    }

    /**
     * Cliente obtiene la factura de un pedido pagado.
     */
    @GetMapping("/{id}/factura")
    public ResponseEntity<FacturaResponse> obtenerFactura(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(pedidoService.obtenerFactura(email, id));
    }
}
