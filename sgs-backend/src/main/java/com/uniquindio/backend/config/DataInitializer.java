package com.uniquindio.backend.config;

import com.uniquindio.backend.model.Rol;
import com.uniquindio.backend.model.Usuario;
import com.uniquindio.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crea un usuario administrador por defecto si no existe.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByEmail("admin@sgssupermercado.com")) {
            Usuario admin = Usuario.builder()
                    .nombre("Admin")
                    .apellido("SGS")
                    .email("admin@sgssupermercado.com")
                    .password(passwordEncoder.encode("Admin123"))
                    .rol(Rol.ADMIN)
                    .activo(true)
                    .build();
            usuarioRepository.save(admin);
            System.out.println(">>> Usuario administrador creado: admin@sgssupermercado.com / Admin123");
        }
    }
}
