package com.uniquindio.backend.service;

import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.uniquindio.backend.config.AppProperties;
import com.uniquindio.backend.config.MercadoPagoProperties;
import com.uniquindio.backend.dto.*;
import com.uniquindio.backend.model.*;
import com.uniquindio.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final InventarioService inventarioService;
    private final MercadoPagoProperties mercadoPagoProperties;
    private final AppProperties appProperties;

    /**
     * Crea el pedido en BD con estado PENDIENTE y genera la preferencia de Mercado Pago.
     */
    @Transactional
    public PreferenciaResponse crearPreferencia(String emailUsuario, PedidoRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<DetallePedido> detalles = new ArrayList<>();
        List<PreferenceItemRequest> mpItems = new ArrayList<>();
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

            // Descontar stock
            producto.setStock(producto.getStock() - item.cantidad());
            productoRepository.save(producto);
            inventarioService.sincronizarProducto(producto);

            // Item para preferencia de MP
            mpItems.add(PreferenceItemRequest.builder()
                    .id(String.valueOf(producto.getId()))
                    .title(producto.getNombre())
                    .quantity(item.cantidad())
                    .unitPrice(producto.getPrecio())
                    .currencyId("COP")
                    .build());
        }

        // Crear pedido en BD
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

        // Crear preferencia de Mercado Pago
        try {
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(appProperties.getFrontendUrl() + "/mis-pedidos")
                    .failure(appProperties.getFrontendUrl() + "/carrito")
                    .pending(appProperties.getFrontendUrl() + "/mis-pedidos")
                    .build();

            PreferencePayerRequest payer = PreferencePayerRequest.builder()
                    .email(usuario.getEmail())
                    .name(usuario.getNombre())
                    .surname(usuario.getApellido())
                    .identification(IdentificationRequest.builder()
                            .type("CC")
                            .number(usuario.getCedula())
                            .build())
                    .build();

            PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                    .excludedPaymentTypes(List.of())
                    .excludedPaymentMethods(List.of())
                    .installments(1)
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(mpItems)
                    .payer(payer)
                    .backUrls(backUrls)
                    .paymentMethods(paymentMethods)
                    .externalReference(String.valueOf(pedido.getId()))
                    .notificationUrl(mercadoPagoProperties.getNotificationUrl())
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            log.info("Preferencia MP creada: {} para pedido: {}", preference.getId(), pedido.getId());

            return new PreferenciaResponse(pedido.getId(), preference.getInitPoint());

        } catch (MPApiException e) {
            log.error("MP API Error - Status: {}, Content: {}", e.getStatusCode(), e.getApiResponse().getContent());
            throw new RuntimeException("Error de Mercado Pago: " + e.getApiResponse().getContent());
        } catch (MPException e) {
            log.error("Error al crear preferencia de Mercado Pago", e);
            throw new RuntimeException("Error al conectar con Mercado Pago: " + e.getMessage());
        }
    }

    /**
     * Procesa la notificación de webhook de Mercado Pago.
     */
    @Transactional
    public void procesarWebhook(String topic, Long resourceId) {
        if (!"payment".equals(topic)) {
            log.info("Webhook ignorado, topic: {}", topic);
            return;
        }

        try {
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(resourceId);

            String externalReference = payment.getExternalReference();
            String status = payment.getStatus();
            String paymentId = String.valueOf(payment.getId());

            log.info("Webhook recibido - Payment ID: {}, Status: {}, Pedido: {}",
                    paymentId, status, externalReference);

            Long pedidoId = Long.parseLong(externalReference);
            Pedido pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + pedidoId));

            pedido.setMercadoPagoPaymentId(paymentId);

            switch (status) {
                case "approved" -> {
                    pedido.setEstado(EstadoPedido.PAGADO);
                    pedido.setFechaUltimoCambioEstado(LocalDateTime.now());
                    log.info("Pedido {} marcado como PAGADO", pedidoId);
                }
                case "rejected" -> {
                    pedido.setEstado(EstadoPedido.RECHAZADO);
                    pedido.setFechaUltimoCambioEstado(LocalDateTime.now());
                    devolverStock(pedido);
                    log.info("Pedido {} marcado como RECHAZADO, stock devuelto", pedidoId);
                }
                case "in_process", "pending" -> {
                    log.info("Pedido {} sigue PENDIENTE", pedidoId);
                }
                default -> log.warn("Estado de pago desconocido: {}", status);
            }

            pedidoRepository.save(pedido);

        } catch (MPException | MPApiException e) {
            log.error("Error al consultar pago en Mercado Pago", e);
            throw new RuntimeException("Error al procesar webhook: " + e.getMessage());
        }
    }

    /**
     * Procesa la notificación de webhook v2 (tipo merchant_order o payment).
     */
    @Transactional
    public void procesarWebhookV2(String type, String dataId) {
        if (!"payment".equals(type)) {
            log.info("Webhook v2 ignorado, type: {}", type);
            return;
        }

        procesarWebhook("payment", Long.parseLong(dataId));
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
}
