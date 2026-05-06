package com.uniquindio.backend.controller;

import com.uniquindio.backend.dto.*;
import com.uniquindio.backend.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping
    public ResponseEntity<PerfilResponse> obtenerPerfil(Authentication auth) {
        return ResponseEntity.ok(perfilService.obtenerPerfil(auth.getName()));
    }

    @PutMapping
    public ResponseEntity<PerfilResponse> actualizarPerfil(Authentication auth,
                                                            @Valid @RequestBody PerfilRequest request) {
        return ResponseEntity.ok(perfilService.actualizarPerfil(auth.getName(), request));
    }

    // --- Direcciones ---

    @GetMapping("/direcciones")
    public ResponseEntity<List<DireccionResponse>> listarDirecciones(Authentication auth) {
        return ResponseEntity.ok(perfilService.listarDirecciones(auth.getName()));
    }

    @PostMapping("/direcciones")
    public ResponseEntity<DireccionResponse> crearDireccion(Authentication auth,
                                                             @Valid @RequestBody DireccionRequest request) {
        return ResponseEntity.ok(perfilService.crearDireccion(auth.getName(), request));
    }

    @PutMapping("/direcciones/{id}")
    public ResponseEntity<DireccionResponse> actualizarDireccion(Authentication auth,
                                                                  @PathVariable Long id,
                                                                  @Valid @RequestBody DireccionRequest request) {
        return ResponseEntity.ok(perfilService.actualizarDireccion(auth.getName(), id, request));
    }

    @DeleteMapping("/direcciones/{id}")
    public ResponseEntity<Void> eliminarDireccion(Authentication auth, @PathVariable Long id) {
        perfilService.eliminarDireccion(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/direcciones/{id}/predeterminada")
    public ResponseEntity<DireccionResponse> marcarPredeterminada(Authentication auth, @PathVariable Long id) {
        return ResponseEntity.ok(perfilService.marcarPredeterminada(auth.getName(), id));
    }
}
