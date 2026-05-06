package com.uniquindio.backend.controller;

import com.uniquindio.backend.dto.ClienteResponse;
import com.uniquindio.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteResponse>> listarClientes() {
        return ResponseEntity.ok(adminService.listarClientes());
    }
}
