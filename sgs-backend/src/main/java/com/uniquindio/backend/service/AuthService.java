package com.uniquindio.backend.service;

import com.uniquindio.backend.dto.*;
import com.uniquindio.backend.model.Rol;
import com.uniquindio.backend.model.Usuario;
import com.uniquindio.backend.repository.UsuarioRepository;
import com.uniquindio.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    /**
     * Registra un nuevo usuario con rol CLIENTE.
     * Envía un código de verificación de 6 dígitos al correo.
     */
    @Transactional
    public MensajeResponse registrar(RegistroRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Ya existe una cuenta registrada con este email");
        }

        if (usuarioRepository.existsByCedula(request.cedula())) {
            throw new RuntimeException("Ya existe una cuenta registrada con esta cédula");
        }

        String codigo = generarCodigo();

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .cedula(request.cedula())
                .telefono(request.telefono())
                .rol(Rol.CLIENTE)
                .activo(false)
                .codigoVerificacion(codigo)
                .fechaExpiracionCodigo(LocalDateTime.now().plusMinutes(15))
                .build();

        usuarioRepository.save(usuario);
        emailService.enviarCodigoVerificacion(request.email(), codigo);

        return new MensajeResponse(
                "Registro exitoso. Se ha enviado un código de verificación a tu correo electrónico."
        );
    }

    /**
     * Verifica la cuenta con el código de 6 dígitos enviado al correo.
     */
    @Transactional
    public MensajeResponse verificarCuenta(VerificacionRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("No se encontró una cuenta con este email"));

        if (usuario.isActivo()) {
            throw new RuntimeException("La cuenta ya está verificada");
        }

        if (usuario.getFechaExpiracionCodigo().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El código de verificación ha expirado. Solicita uno nuevo.");
        }

        if (!usuario.getCodigoVerificacion().equals(request.codigo())) {
            throw new RuntimeException("Código de verificación incorrecto");
        }

        usuario.setActivo(true);
        usuario.setCodigoVerificacion(null);
        usuario.setFechaExpiracionCodigo(null);
        usuarioRepository.save(usuario);

        return new MensajeResponse("Cuenta verificada exitosamente. Ya puedes iniciar sesión.");
    }

    /**
     * Inicia sesión y retorna un JWT.
     */
    public AuthResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        if (!usuario.isActivo()) {
            throw new RuntimeException("Tu cuenta no está verificada. Revisa tu correo electrónico.");
        }

        Map<String, Object> claims = Map.of(
                "rol", usuario.getRol().name(),
                "nombre", usuario.getNombre()
        );

        String token = jwtService.generarToken(usuario.getEmail(), claims);

        return new AuthResponse(
                token,
                usuario.getEmail(),
                usuario.getNombre() + " " + usuario.getApellido(),
                usuario.getRol().name()
        );
    }

    /**
     * Reenvía el código de verificación al correo del usuario.
     */
    @Transactional
    public MensajeResponse reenviarCodigo(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No se encontró una cuenta con este email"));

        if (usuario.isActivo()) {
            throw new RuntimeException("La cuenta ya está verificada");
        }

        String nuevoCodigo = generarCodigo();
        usuario.setCodigoVerificacion(nuevoCodigo);
        usuario.setFechaExpiracionCodigo(LocalDateTime.now().plusMinutes(15));
        usuarioRepository.save(usuario);

        emailService.enviarCodigoVerificacion(email, nuevoCodigo);

        return new MensajeResponse("Se ha reenviado el código de verificación a tu correo.");
    }

    private String generarCodigo() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
