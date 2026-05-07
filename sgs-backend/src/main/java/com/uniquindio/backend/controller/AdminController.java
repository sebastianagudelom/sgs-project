package com.uniquindio.backend.controller;

import com.uniquindio.backend.dto.*;
import com.uniquindio.backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ===== Clientes =====

    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteResponse>> listarClientes() {
        return ResponseEntity.ok(adminService.listarClientes());
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<ClienteDetalleResponse> obtenerCliente(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.obtenerCliente(id));
    }

    @PostMapping("/clientes")
    public ResponseEntity<ClienteResponse> crearCliente(@Valid @RequestBody ClienteAdminRequest request) {
        return ResponseEntity.ok(adminService.crearCliente(request));
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<ClienteResponse> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteAdminRequest request) {
        return ResponseEntity.ok(adminService.actualizarCliente(id, request));
    }

    @PatchMapping("/clientes/{id}/estado")
    public ResponseEntity<ClienteResponse> cambiarEstado(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.cambiarEstado(id));
    }

    @PatchMapping("/clientes/{id}/password")
    public ResponseEntity<MensajeResponse> resetPassword(
            @PathVariable Long id,
            @Valid @RequestBody ResetPasswordRequest request) {
        adminService.resetearPassword(id, request);
        return ResponseEntity.ok(new MensajeResponse("Contraseña actualizada"));
    }

    // ===== Direcciones del cliente =====

    @GetMapping("/clientes/{id}/direcciones")
    public ResponseEntity<List<DireccionResponse>> listarDirecciones(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.listarDireccionesCliente(id));
    }

    @PostMapping("/clientes/{id}/direcciones")
    public ResponseEntity<DireccionResponse> crearDireccion(
            @PathVariable Long id,
            @Valid @RequestBody DireccionRequest request) {
        return ResponseEntity.ok(adminService.crearDireccionCliente(id, request));
    }

    @PutMapping("/clientes/{id}/direcciones/{direccionId}")
    public ResponseEntity<DireccionResponse> actualizarDireccion(
            @PathVariable Long id,
            @PathVariable Long direccionId,
            @Valid @RequestBody DireccionRequest request) {
        return ResponseEntity.ok(adminService.actualizarDireccionCliente(id, direccionId, request));
    }

    @DeleteMapping("/clientes/{id}/direcciones/{direccionId}")
    public ResponseEntity<MensajeResponse> eliminarDireccion(
            @PathVariable Long id,
            @PathVariable Long direccionId) {
        adminService.eliminarDireccionCliente(id, direccionId);
        return ResponseEntity.ok(new MensajeResponse("Dirección eliminada"));
    }
}
