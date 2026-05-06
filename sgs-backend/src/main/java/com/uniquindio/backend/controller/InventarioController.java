package com.uniquindio.backend.controller;

import com.uniquindio.backend.dto.AlertaInventarioResponse;
import com.uniquindio.backend.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping("/alertas")
    public ResponseEntity<List<AlertaInventarioResponse>> listarAlertas() {
        return ResponseEntity.ok(inventarioService.verificarStockBajo());
    }

    @PostMapping("/verificar")
    public ResponseEntity<List<AlertaInventarioResponse>> verificarStock() {
        return ResponseEntity.ok(inventarioService.verificarStockBajo());
    }

    @GetMapping("/alertas/contador")
    public ResponseEntity<Map<String, Long>> contarAlertasActivas() {
        return ResponseEntity.ok(Map.of("total", inventarioService.contarAlertasActivas()));
    }

    @PatchMapping("/alertas/{id}/leida")
    public ResponseEntity<AlertaInventarioResponse> marcarLeida(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.marcarComoLeida(id));
    }
}
