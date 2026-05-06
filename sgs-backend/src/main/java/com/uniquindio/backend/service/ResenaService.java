package com.uniquindio.backend.service;

import com.uniquindio.backend.dto.*;
import com.uniquindio.backend.model.*;
import com.uniquindio.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private static final List<EstadoPedido> ESTADOS_COMPRA_VERIFICADA = List.of(
            EstadoPedido.PAGADO,
            EstadoPedido.CONFIRMADO,
            EstadoPedido.ENVIADO,
            EstadoPedido.ENTREGADO
    );

    private final ResenaRepository resenaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    @Transactional(readOnly = true)
    public List<ResenaResponse> listarPorProducto(Long productoId) {
        validarProducto(productoId);
        return resenaRepository.findByProductoIdOrderByFechaCreacionDesc(productoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResenaResumenResponse obtenerResumen(Long productoId) {
        validarProducto(productoId);
        return new ResenaResumenResponse(
                resenaRepository.obtenerPromedioPorProducto(productoId),
                resenaRepository.countByProductoId(productoId)
        );
    }

    @Transactional(readOnly = true)
    public MiEstadoResenaResponse obtenerMiEstado(String emailUsuario, Long productoId) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        validarProducto(productoId);
        boolean compraVerificada = tieneCompraVerificada(usuario.getId(), productoId);
        ResenaResponse miResena = resenaRepository.findByProductoIdAndUsuarioId(productoId, usuario.getId())
                .map(this::toResponse)
                .orElse(null);

        return new MiEstadoResenaResponse(
                compraVerificada,
                miResena != null,
                compraVerificada && miResena == null,
                miResena
        );
    }

    @Transactional
    public ResenaResponse crear(String emailUsuario, Long productoId, ResenaRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        validarCompraParaResena(usuario.getId(), productoId);

        if (resenaRepository.existsByProductoIdAndUsuarioId(productoId, usuario.getId())) {
            throw new RuntimeException("Ya registraste una resena para este producto");
        }

        Resena resena = Resena.builder()
                .producto(producto)
                .usuario(usuario)
                .calificacion(request.calificacion())
                .comentario(request.comentario().trim())
                .build();

        return toResponse(resenaRepository.save(resena));
    }

    @Transactional
    public ResenaResponse actualizar(String emailUsuario, Long resenaId, ResenaRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Resena resena = resenaRepository.findById(resenaId)
                .orElseThrow(() -> new RuntimeException("Resena no encontrada"));

        if (!resena.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para modificar esta resena");
        }

        resena.setCalificacion(request.calificacion());
        resena.setComentario(request.comentario().trim());
        return toResponse(resenaRepository.save(resena));
    }

    @Transactional
    public void eliminar(String emailUsuario, Long resenaId) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Resena resena = resenaRepository.findById(resenaId)
                .orElseThrow(() -> new RuntimeException("Resena no encontrada"));

        boolean esAutor = resena.getUsuario().getId().equals(usuario.getId());
        boolean esAdmin = usuario.getRol() == Rol.ADMIN;

        if (!esAutor && !esAdmin) {
            throw new RuntimeException("No tienes permiso para eliminar esta resena");
        }

        resenaRepository.delete(resena);
    }

    private void validarProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new RuntimeException("Producto no encontrado");
        }
    }

    private void validarCompraParaResena(Long usuarioId, Long productoId) {
        if (!tieneCompraVerificada(usuarioId, productoId)) {
            throw new RuntimeException("Solo puedes resenar productos que ya compraste");
        }
    }

    private boolean tieneCompraVerificada(Long usuarioId, Long productoId) {
        return detallePedidoRepository.existsByPedidoUsuarioIdAndProductoIdAndPedidoEstadoIn(
                usuarioId,
                productoId,
                ESTADOS_COMPRA_VERIFICADA
        );
    }

    private ResenaResponse toResponse(Resena resena) {
        Usuario usuario = resena.getUsuario();
        return new ResenaResponse(
                resena.getId(),
                resena.getProducto().getId(),
                usuario.getId(),
                usuario.getNombre() + " " + usuario.getApellido(),
                resena.getCalificacion(),
                resena.getComentario(),
                resena.getFechaCreacion(),
                resena.getFechaActualizacion()
        );
    }
}
