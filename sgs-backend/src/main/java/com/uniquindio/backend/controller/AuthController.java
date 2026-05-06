package com.uniquindio.backend.controller;

import com.uniquindio.backend.dto.*;
import com.uniquindio.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<MensajeResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.ok(authService.registrar(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verificar")
    public ResponseEntity<MensajeResponse> verificar(@Valid @RequestBody VerificacionRequest request) {
        return ResponseEntity.ok(authService.verificarCuenta(request));
    }

    @PostMapping("/reenviar-codigo")
    public ResponseEntity<MensajeResponse> reenviarCodigo(@RequestParam String email) {
        return ResponseEntity.ok(authService.reenviarCodigo(email));
    }
}
