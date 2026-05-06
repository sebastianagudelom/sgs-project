package com.uniquindio.backend.service;

import com.uniquindio.backend.dto.*;
import com.uniquindio.backend.model.*;
import com.uniquindio.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final InventarioService inventarioService;

    // Orden de progresión de estados (solo estados "adelante")
    private static final Map<EstadoPedido, Integer> ORDEN_ESTADO = Map.of(
            EstadoPedido.PENDIENTE, 0,
            EstadoPedido.PAGADO, 1,
            EstadoPedido.CONFIRMADO, 2,
            EstadoPedido.ENVIADO, 3,
            EstadoPedido.ENTREGADO, 4
    );

    @Transactional
    public PedidoResponse crearPedido(String emailUsuario, PedidoRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemCarritoRequest item : request.items()) {
            Producto producto = productoRepository.findById(item.productoId())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado: ID " + item.productoId()));

            if (!producto.isActivo()) {
                throw new RuntimeException("El producto '" + producto.getNombre() + "' no está disponible");
            }

            if (producto.getStock() < item.cantidad()) {
                throw new RuntimeException(
                        "Stock insuficiente para '" + producto.getNombre() +
                                "'. Disponible: " + producto.getStock() +
                                ", solicitado: " + item.cantidad());
            }

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.cantidad()));

            DetallePedido detalle = DetallePedido.builder()
                    .producto(producto)
                    .cantidad(item.cantidad())
                    .precioUnitario(producto.getPrecio())
                    .subtotal(subtotal)
                    .build();

            detalles.add(detalle);
            total = total.add(subtotal);

            producto.setStock(producto.getStock() - item.cantidad());
            productoRepository.save(producto);
            inventarioService.sincronizarProducto(producto);
        }

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .estado(EstadoPedido.PENDIENTE)
                .total(total)
                .direccionEnvio(request.direccionEnvio())
                .build();

        pedido = pedidoRepository.save(pedido);

        for (DetallePedido detalle : detalles) {
            detalle.setPedido(pedido);
        }
        pedido.setDetalles(detalles);
        pedido = pedidoRepository.save(pedido);

        return toResponse(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarMisPedidos(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return pedidoRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuario.getId())
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarTodos() {
        return pedidoRepository.findAllByOrderByFechaCreacionDesc()
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return toResponse(pedido);
    }

    /**
     * Actualiza el estado de un pedido (admin).
     * Aplica máquina de estados unidireccional: no se puede retroceder.
     * ENTREGADO, CANCELADO y RECHAZADO son estados terminales.
     */
    @Transactional
    public PedidoResponse actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        EstadoPedido estado;
        try {
            estado = EstadoPedido.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + nuevoEstado);
        }

        validarTransicion(pedido.getEstado(), estado);

        if (estado == EstadoPedido.CANCELADO) {
            devolverStock(pedido);
        }

        pedido.setEstado(estado);
        pedido.setFechaUltimoCambioEstado(LocalDateTime.now());
        return toResponse(pedidoRepository.save(pedido));
    }

    /**
     * Permite al cliente cancelar su propio pedido si está en PENDIENTE.
     */
    @Transactional
    public PedidoResponse cancelarPedidoUsuario(String emailUsuario, Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getUsuario().getEmail().equals(emailUsuario)) {
            throw new RuntimeException("No tienes permiso para cancelar este pedido");
        }
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new RuntimeException("Solo puedes cancelar pedidos en estado PENDIENTE");
        }

        devolverStock(pedido);
        pedido.setEstado(EstadoPedido.CANCELADO);
        pedido.setFechaUltimoCambioEstado(LocalDateTime.now());
        return toResponse(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public FacturaResponse obtenerFactura(String emailUsuario, Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getUsuario().getEmail().equals(emailUsuario)) {
            throw new RuntimeException("No tienes permiso para ver esta factura");
        }

        if (pedido.getEstado() != EstadoPedido.PAGADO
                && pedido.getEstado() != EstadoPedido.CONFIRMADO
                && pedido.getEstado() != EstadoPedido.ENVIADO
                && pedido.getEstado() != EstadoPedido.ENTREGADO) {
            throw new RuntimeException("La factura solo está disponible para pedidos pagados");
        }

        List<DetallePedidoResponse> detallesDto = pedido.getDetalles() != null
                ? pedido.getDetalles().stream().map(d -> new DetallePedidoResponse(
                d.getId(),
                d.getProducto().getId(),
                d.getProducto().getNombre(),
                d.getProducto().getImagenUrl(),
                d.getCantidad(),
                d.getPrecioUnitario(),
                d.getSubtotal()
        )).toList()
                : List.of();

        Usuario usuario = pedido.getUsuario();
        return new FacturaResponse(
                pedido.getId(),
                usuario.getNombre() + " " + usuario.getApellido(),
                usuario.getEmail(),
                usuario.getCedula(),
                usuario.getTelefono(),
                pedido.getDireccionEnvio(),
                pedido.getEstado().name(),
                pedido.getMercadoPagoPaymentId(),
                pedido.getTotal(),
                detallesDto,
                pedido.getFechaActualizacion(),
                pedido.getFechaCreacion()
        );
    }

    // ── Tareas programadas ──────────────────────────────────────────────────────

    /**
     * Cancela automáticamente pedidos PENDIENTE que llevan más de 5 minutos sin pagar.
     * Ejecuta cada minuto.
     */
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void cancelarPedidosPendientesVencidos() {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(5);
        List<Pedido> vencidos = pedidoRepository.findByEstadoAndFechaCreacionBefore(
                EstadoPedido.PENDIENTE, limite);
        for (Pedido pedido : vencidos) {
            devolverStock(pedido);
            pedido.setEstado(EstadoPedido.CANCELADO);
            pedido.setFechaUltimoCambioEstado(LocalDateTime.now());
            pedidoRepository.save(pedido);
            log.info("Pedido {} cancelado automáticamente por vencimiento (>5 min sin pago)", pedido.getId());
        }
    }

    /**
     * Avanza automáticamente el estado de los pedidos pagados:
     * PAGADO → CONFIRMADO → ENVIADO → ENTREGADO (1 minuto entre cada cambio).
     * Ejecuta cada minuto.
     */
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void avanzarEstadosPedidosPagados() {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(1);

        avanzarEstados(
                pedidoRepository.findByEstadoAndFechaUltimoCambioEstadoBefore(EstadoPedido.PAGADO, limite),
                EstadoPedido.CONFIRMADO);

        avanzarEstados(
                pedidoRepository.findByEstadoAndFechaUltimoCambioEstadoBefore(EstadoPedido.CONFIRMADO, limite),
                EstadoPedido.ENVIADO);

        avanzarEstados(
                pedidoRepository.findByEstadoAndFechaUltimoCambioEstadoBefore(EstadoPedido.ENVIADO, limite),
                EstadoPedido.ENTREGADO);
    }

    // ── Helpers privados ────────────────────────────────────────────────────────

    private void avanzarEstados(List<Pedido> pedidos, EstadoPedido nuevoEstado) {
        for (Pedido pedido : pedidos) {
            pedido.setEstado(nuevoEstado);
            pedido.setFechaUltimoCambioEstado(LocalDateTime.now());
            pedidoRepository.save(pedido);
            log.info("Pedido {} avanzado automáticamente a {}", pedido.getId(), nuevoEstado);
        }
    }

    private void devolverStock(Pedido pedido) {
        if (pedido.getDetalles() == null) return;
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
            inventarioService.sincronizarProducto(producto);
        }
    }

    /**
     * Valida que la transición de estado sea válida (solo hacia adelante).
     * Estados terminales: ENTREGADO, CANCELADO, RECHAZADO.
     */
    private void validarTransicion(EstadoPedido actual, EstadoPedido nuevo) {
        if (actual == EstadoPedido.ENTREGADO) {
            throw new RuntimeException("El pedido ya fue entregado y no puede modificarse");
        }
        if (actual == EstadoPedido.CANCELADO || actual == EstadoPedido.RECHAZADO) {
            throw new RuntimeException("El pedido en estado " + actual + " no puede modificarse");
        }
        // CANCELADO siempre está permitido desde cualquier estado no terminal
        if (nuevo == EstadoPedido.CANCELADO) return;

        Integer ordenActual = ORDEN_ESTADO.get(actual);
        Integer ordenNuevo = ORDEN_ESTADO.get(nuevo);
        if (ordenActual != null && ordenNuevo != null && ordenNuevo <= ordenActual) {
            throw new RuntimeException(
                    "No se puede cambiar el estado de " + actual + " a " + nuevo +
                    ". Solo se permite avanzar el estado.");
        }
    }

    private PedidoResponse toResponse(Pedido pedido) {
        List<DetallePedidoResponse> detallesDto = pedido.getDetalles() != null
                ? pedido.getDetalles().stream().map(d -> new DetallePedidoResponse(
                d.getId(),
                d.getProducto().getId(),
                d.getProducto().getNombre(),
                d.getProducto().getImagenUrl(),
                d.getCantidad(),
                d.getPrecioUnitario(),
                d.getSubtotal()
        )).toList()
                : List.of();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getEstado().name(),
                pedido.getTotal(),
                pedido.getDireccionEnvio(),
                pedido.getUsuario().getNombre() + " " + pedido.getUsuario().getApellido(),
                pedido.getUsuario().getEmail(),
                detallesDto,
                pedido.getFechaCreacion(),
                pedido.getFechaActualizacion()
        );
    }
}
