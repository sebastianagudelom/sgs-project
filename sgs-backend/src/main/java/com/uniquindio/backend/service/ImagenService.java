package com.uniquindio.backend.service;

import com.uniquindio.backend.model.Imagen;
import com.uniquindio.backend.repository.ImagenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagenService {

    private static final List<String> TIPOS_PERMITIDOS = List.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5 MB

    private final ImagenRepository imagenRepository;

    /**
     * Guarda un archivo de imagen en la base de datos y retorna la URL relativa para acceder a ella.
     */
    public String guardarImagen(MultipartFile archivo) throws IOException {
        if (archivo == null || archivo.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }

        if (archivo.getSize() > MAX_SIZE) {
            throw new RuntimeException("El archivo no puede superar los 5 MB");
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !TIPOS_PERMITIDOS.contains(contentType)) {
            throw new RuntimeException("Solo se permiten imágenes (JPEG, PNG, WebP, GIF)");
        }

        String originalFilename = archivo.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            originalFilename = "imagen";
        }

        Imagen imagen = Imagen.builder()
                .nombre(originalFilename)
                .tipo(contentType)
                .datos(archivo.getBytes())
                .build();

        Imagen guardada = imagenRepository.save(imagen);

        return "/api/imagenes/" + guardada.getId();
    }

    public Imagen obtenerPorId(Long id) {
        return imagenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
    }
}
