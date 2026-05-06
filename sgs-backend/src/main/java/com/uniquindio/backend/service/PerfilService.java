package com.uniquindio.backend.service;

import com.uniquindio.backend.dto.*;
import com.uniquindio.backend.model.Direccion;
import com.uniquindio.backend.model.Usuario;
import com.uniquindio.backend.repository.DireccionRepository;
import com.uniquindio.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;

    public PerfilResponse obtenerPerfil(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return toPerfilResponse(usuario);
    }

    @Transactional
    public PerfilResponse actualizarPerfil(String email, PerfilRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que la cédula no pertenezca a otro usuario
        usuarioRepository.findByCedula(request.cedula()).ifPresent(otro -> {
            if (!otro.getId().equals(usuario.getId())) {
                throw new RuntimeException("La cédula ya está registrada por otro usuario");
            }
        });

        usuario.setNombre(request.nombre());
        usuario.setApellido(request.apellido());
        usuario.setCedula(request.cedula());
        usuario.setTelefono(request.telefono());
        usuarioRepository.save(usuario);

        return toPerfilResponse(usuario);
    }

    // --- Direcciones ---

    public List<DireccionResponse> listarDirecciones(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return direccionRepository.findByUsuarioIdOrderByPredeterminadaDesc(usuario.getId())
                .stream().map(this::toDireccionResponse).toList();
    }

    @Transactional
    public DireccionResponse crearDireccion(String email, DireccionRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        validarCoordenadas(request);

        // Si es predeterminada, quitar predeterminada de las demás
        if (request.predeterminada()) {
            quitarPredeterminadas(usuario.getId());
        }

        // Si es la primera dirección, hacerla predeterminada
        List<Direccion> existentes = direccionRepository.findByUsuarioIdOrderByPredeterminadaDesc(usuario.getId());
        boolean esPredeterminada = request.predeterminada() || existentes.isEmpty();

        Direccion direccion = Direccion.builder()
                .nombre(request.nombre())
                .direccion(request.direccion())
                .latitud(request.latitud())
                .longitud(request.longitud())
                .predeterminada(esPredeterminada)
                .usuario(usuario)
                .build();

        return toDireccionResponse(direccionRepository.save(direccion));
    }

    @Transactional
    public DireccionResponse actualizarDireccion(String email, Long direccionId, DireccionRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Direccion direccion = direccionRepository.findById(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        if (!direccion.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para modificar esta dirección");
        }

        validarCoordenadas(request);

        if (request.predeterminada()) {
            quitarPredeterminadas(usuario.getId());
        }

        direccion.setNombre(request.nombre());
        direccion.setDireccion(request.direccion());
        direccion.setLatitud(request.latitud());
        direccion.setLongitud(request.longitud());
        direccion.setPredeterminada(request.predeterminada());

        return toDireccionResponse(direccionRepository.save(direccion));
    }

    @Transactional
    public void eliminarDireccion(String email, Long direccionId) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Direccion direccion = direccionRepository.findById(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        if (!direccion.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar esta dirección");
        }

        boolean eraPredeterminada = direccion.isPredeterminada();
        direccionRepository.delete(direccion);

        // Si era predeterminada, asignar otra
        if (eraPredeterminada) {
            List<Direccion> restantes = direccionRepository.findByUsuarioIdOrderByPredeterminadaDesc(usuario.getId());
            if (!restantes.isEmpty()) {
                restantes.get(0).setPredeterminada(true);
                direccionRepository.save(restantes.get(0));
            }
        }
    }

    @Transactional
    public DireccionResponse marcarPredeterminada(String email, Long direccionId) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Direccion direccion = direccionRepository.findById(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        if (!direccion.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para modificar esta dirección");
        }

        quitarPredeterminadas(usuario.getId());
        direccion.setPredeterminada(true);
        return toDireccionResponse(direccionRepository.save(direccion));
    }

    private void quitarPredeterminadas(Long usuarioId) {
        direccionRepository.findByUsuarioIdAndPredeterminadaTrue(usuarioId)
                .ifPresent(d -> {
                    d.setPredeterminada(false);
                    direccionRepository.save(d);
                });
    }

    private void validarCoordenadas(DireccionRequest request) {
        if ((request.latitud() == null) != (request.longitud() == null)) {
            throw new RuntimeException("Debes enviar latitud y longitud juntas");
        }
    }

    private PerfilResponse toPerfilResponse(Usuario usuario) {
        List<DireccionResponse> dirs = direccionRepository
                .findByUsuarioIdOrderByPredeterminadaDesc(usuario.getId())
                .stream().map(this::toDireccionResponse).toList();

        return new PerfilResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getCedula(),
                usuario.getTelefono(),
                dirs
        );
    }

    private DireccionResponse toDireccionResponse(Direccion d) {
        return new DireccionResponse(
                d.getId(),
                d.getNombre(),
                d.getDireccion(),
                d.getLatitud(),
                d.getLongitud(),
                d.isPredeterminada()
        );
    }
}
