package com.uniquindio.backend.repository;

import com.uniquindio.backend.model.Rol;
import com.uniquindio.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCedula(String cedula);

    Optional<Usuario> findByCedula(String cedula);

    List<Usuario> findByRolOrderByFechaCreacionDesc(Rol rol);
}
