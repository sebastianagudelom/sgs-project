package com.uniquindio.backend.repository;

import com.uniquindio.backend.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {

    List<Direccion> findByUsuarioIdOrderByPredeterminadaDesc(Long usuarioId);

    Optional<Direccion> findByUsuarioIdAndPredeterminadaTrue(Long usuarioId);
}
