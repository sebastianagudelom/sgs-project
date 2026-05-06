package com.uniquindio.backend.controller;

import com.uniquindio.backend.dto.*;
import com.uniquindio.backend.service.ResenaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<ResenaResponse>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(resenaService.listarPorProducto(productoId));
    }

    @GetMapping("/producto/{productoId}/resumen")
    public ResponseEntity<ResenaResumenResponse> obtenerResumen(@PathVariable Long productoId) {
        return ResponseEntity.ok(resenaService.obtenerResumen(productoId));
    }

    @GetMapping("/producto/{productoId}/mi-estado")
    public ResponseEntity<MiEstadoResenaResponse> obtenerMiEstado(
            @PathVariable Long productoId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(resenaService.obtenerMiEstado(authentication.getName(), productoId));
    }

    @PostMapping("/producto/{productoId}")
    public ResponseEntity<ResenaResponse> crear(
            @PathVariable Long productoId,
            @Valid @RequestBody ResenaRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(resenaService.crear(authentication.getName(), productoId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResenaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResenaRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(resenaService.actualizar(authentication.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication authentication) {
        resenaService.eliminar(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
