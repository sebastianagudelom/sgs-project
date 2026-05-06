package com.uniquindio.backend.controller;

import com.uniquindio.backend.model.Imagen;
import com.uniquindio.backend.service.ImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
public class ImagenController {

    private final ImagenService imagenService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> subirImagen(@RequestParam("archivo") MultipartFile archivo) throws IOException {
        String url = imagenService.guardarImagen(archivo);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Long id) {
        Imagen imagen = imagenService.obtenerPorId(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(imagen.getTipo()));
        headers.setContentLength(imagen.getDatos().length);
        headers.setCacheControl("public, max-age=86400");

        return new ResponseEntity<>(imagen.getDatos(), headers, HttpStatus.OK);
    }
}
