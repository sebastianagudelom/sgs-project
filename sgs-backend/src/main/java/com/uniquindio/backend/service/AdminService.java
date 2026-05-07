package com.uniquindio.backend.service;

import com.uniquindio.backend.dto.*;
import com.uniquindio.backend.model.*;
import com.uniquindio.backend.repository.DireccionRepository;
import com.uniquindio.backend.repository.PedidoRepository;
import com.uniquindio.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {

    private static final Set<EstadoPedido> ESTADOS_PAGADOS = Set.of(
            EstadoPedido.PAGADO, EstadoPedido.CONFIRMADO,
            EstadoPedido.ENVIADO, EstadoPedido.ENTREGADO);

    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final DireccionRepository direccionRepository;
    private final PasswordEncoder passwordEncoder;

    // ===== Clientes (lista y detalle) =====

    public List<ClienteResponse> listarClientes() {
        List<Usuario> clientes = usuarioRepository.findByRolOrderByFechaCreacionDesc(Rol.CLIENTE);
        return clientes.stream().map(this::toClienteResponse).toList();
    }

    public ClienteDetalleResponse obtenerCliente(Long id) {
        Usuario cliente = buscarClienteOrFail(id);
        Estadisticas stats = calcularEstadisticas(cliente.getId());
        List<DireccionResponse> direcciones = direccionRepository
                .findByUsuarioIdOrderByPredeterminadaDesc(cliente.getId())
                .stream().map(this::toDireccionResponse).toList();

        return new ClienteDetalleResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getEmail(),
                cliente.getCedula(),
                cliente.getTelefono(),
                cliente.isActivo(),
                stats.total(),
                stats.gastado(),
                cliente.getFechaCreacion(),
                direcciones
        );
    }

    // ===== Crear cliente =====

    @Transactional
    public ClienteResponse crearCliente(ClienteAdminRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (request.cedula() != null && !request.cedula().isBlank()
                && usuarioRepository.existsByCedula(request.cedula())) {
            throw new RuntimeException("La cédula ya está registrada");
        }
        if (request.password() == null || request.password().length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }

        Usuario cliente = Usuario.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .email(request.email())
                .cedula(toNullIfBlank(request.cedula()))
                .telefono(toNullIfBlank(request.telefono()))
                .password(passwordEncoder.encode(request.password()))
                .rol(Rol.CLIENTE)
                .activo(true)
                .build();

        return toClienteResponse(usuarioRepository.save(cliente));
    }

    // ===== Actualizar cliente =====

    @Transactional
    public ClienteResponse actualizarCliente(Long id, ClienteAdminRequest request) {
        Usuario cliente = buscarClienteOrFail(id);

        // Email único
        usuarioRepository.findByEmail(request.email()).ifPresent(otro -> {
            if (!otro.getId().equals(cliente.getId())) {
                throw new RuntimeException("El email ya está registrado por otro usuario");
            }
        });
        // Cédula única (si viene)
        if (request.cedula() != null && !request.cedula().isBlank()) {
            usuarioRepository.findByCedula(request.cedula()).ifPresent(otro -> {
                if (!otro.getId().equals(cliente.getId())) {
                    throw new RuntimeException("La cédula ya está registrada por otro usuario");
                }
            });
        }

        cliente.setNombre(request.nombre());
        cliente.setApellido(request.apellido());
        cliente.setEmail(request.email());
        cliente.setCedula(toNullIfBlank(request.cedula()));
        cliente.setTelefono(toNullIfBlank(request.telefono()));
        return toClienteResponse(usuarioRepository.save(cliente));
    }

    // ===== Activar / Desactivar =====

    @Transactional
    public ClienteResponse cambiarEstado(Long id) {
        Usuario cliente = buscarClienteOrFail(id);
        cliente.setActivo(!cliente.isActivo());
        return toClienteResponse(usuarioRepository.save(cliente));
    }

    // ===== Reset password =====

    @Transactional
    public void resetearPassword(Long id, ResetPasswordRequest request) {
        Usuario cliente = buscarClienteOrFail(id);
        cliente.setPassword(passwordEncoder.encode(request.nuevaPassword()));
        usuarioRepository.save(cliente);
    }

    // ===== Direcciones del cliente =====

    public List<DireccionResponse> listarDireccionesCliente(Long clienteId) {
        Usuario cliente = buscarClienteOrFail(clienteId);
        return direccionRepository.findByUsuarioIdOrderByPredeterminadaDesc(cliente.getId())
                .stream().map(this::toDireccionResponse).toList();
    }

    @Transactional
    public DireccionResponse crearDireccionCliente(Long clienteId, DireccionRequest request) {
        Usuario cliente = buscarClienteOrFail(clienteId);
        validarCoordenadas(request);

        if (request.predeterminada()) {
            quitarPredeterminadas(cliente.getId());
        }
        List<Direccion> existentes = direccionRepository.findByUsuarioIdOrderByPredeterminadaDesc(cliente.getId());
        boolean esPredeterminada = request.predeterminada() || existentes.isEmpty();

        Direccion direccion = Direccion.builder()
                .nombre(request.nombre())
                .direccion(request.direccion())
                .latitud(request.latitud())
                .longitud(request.longitud())
                .predeterminada(esPredeterminada)
                .usuario(cliente)
                .build();

        return toDireccionResponse(direccionRepository.save(direccion));
    }

    @Transactional
    public DireccionResponse actualizarDireccionCliente(Long clienteId, Long direccionId, DireccionRequest request) {
        Usuario cliente = buscarClienteOrFail(clienteId);
        Direccion direccion = direccionRepository.findById(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        if (!direccion.getUsuario().getId().equals(cliente.getId())) {
            throw new RuntimeException("La dirección no pertenece a este cliente");
        }

        validarCoordenadas(request);
        if (request.predeterminada()) {
            quitarPredeterminadas(cliente.getId());
        }

        direccion.setNombre(request.nombre());
        direccion.setDireccion(request.direccion());
        direccion.setLatitud(request.latitud());
        direccion.setLongitud(request.longitud());
        direccion.setPredeterminada(request.predeterminada());

        return toDireccionResponse(direccionRepository.save(direccion));
    }

    @Transactional
    public void eliminarDireccionCliente(Long clienteId, Long direccionId) {
        Usuario cliente = buscarClienteOrFail(clienteId);
        Direccion direccion = direccionRepository.findById(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        if (!direccion.getUsuario().getId().equals(cliente.getId())) {
            throw new RuntimeException("La dirección no pertenece a este cliente");
        }

        boolean eraPredeterminada = direccion.isPredeterminada();
        direccionRepository.delete(direccion);

        if (eraPredeterminada) {
            List<Direccion> restantes = direccionRepository.findByUsuarioIdOrderByPredeterminadaDesc(cliente.getId());
            if (!restantes.isEmpty()) {
                restantes.get(0).setPredeterminada(true);
                direccionRepository.save(restantes.get(0));
            }
        }
    }

    // ===== Helpers =====

    private Usuario buscarClienteOrFail(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        if (u.getRol() != Rol.CLIENTE) {
            throw new RuntimeException("El usuario no es un cliente");
        }
        return u;
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

    private String toNullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    private Estadisticas calcularEstadisticas(Long usuarioId) {
        List<Pedido> pedidosPagados = pedidoRepository
                .findByUsuarioIdOrderByFechaCreacionDesc(usuarioId)
                .stream()
                .filter(p -> ESTADOS_PAGADOS.contains(p.getEstado()))
                .toList();
        BigDecimal gastado = pedidosPagados.stream()
                .map(Pedido::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Estadisticas(pedidosPagados.size(), gastado);
    }

    private ClienteResponse toClienteResponse(Usuario u) {
        Estadisticas stats = calcularEstadisticas(u.getId());
        return new ClienteResponse(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getCedula(),
                u.getTelefono(),
                u.isActivo(),
                stats.total(),
                stats.gastado(),
                u.getFechaCreacion()
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

    private record Estadisticas(long total, BigDecimal gastado) {}
}
