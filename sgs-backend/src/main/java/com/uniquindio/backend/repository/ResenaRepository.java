package com.uniquindio.backend.repository;

import com.uniquindio.backend.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResenaRepository extends JpaRepository<Resena, Long> {

    List<Resena> findByProductoIdOrderByFechaCreacionDesc(Long productoId);

    Optional<Resena> findByProductoIdAndUsuarioId(Long productoId, Long usuarioId);

    boolean existsByProductoIdAndUsuarioId(Long productoId, Long usuarioId);

    long countByProductoId(Long productoId);

    @Query("select coalesce(avg(r.calificacion), 0.0) from Resena r where r.producto.id = :productoId")
    double obtenerPromedioPorProducto(@Param("productoId") Long productoId);
}
