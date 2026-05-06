package com.uniquindio.backend.service;

import com.uniquindio.backend.dto.AlertaInventarioResponse;
import com.uniquindio.backend.model.*;
import com.uniquindio.backend.repository.AlertaInventarioRepository;
import com.uniquindio.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private static final List<EstadoAlertaInventario> ALERTAS_VIGENTES = List.of(
            EstadoAlertaInventario.ACTIVA,
            EstadoAlertaInventario.LEIDA
    );

    private final ProductoRepository productoRepository;
    private final AlertaInventarioRepository alertaInventarioRepository;

    @Transactional
    public List<AlertaInventarioResponse> verificarStockBajo() {
        productoRepository.findAll().forEach(this::sincronizarProducto);
        return listarAlertasVigentes();
    }

    @Transactional(readOnly = true)
    public List<AlertaInventarioResponse> listarAlertasVigentes() {
        return alertaInventarioRepository.findByEstadoInOrderByFechaCreacionDesc(ALERTAS_VIGENTES)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public long contarAlertasActivas() {
        return alertaInventarioRepository.countByEstadoIn(List.of(EstadoAlertaInventario.ACTIVA));
    }

    @Transactional
    public AlertaInventarioResponse marcarComoLeida(Long alertaId) {
        AlertaInventario alerta = alertaInventarioRepository.findById(alertaId)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));

        if (alerta.getEstado() == EstadoAlertaInventario.ACTIVA) {
            alerta.setEstado(EstadoAlertaInventario.LEIDA);
        }

        return toResponse(alertaInventarioRepository.save(alerta));
    }

    @Transactional
    public void sincronizarProducto(Producto producto) {
        int umbral = obtenerUmbral(producto);
        boolean stockBajo = producto.isActivo() && producto.getStock() != null && producto.getStock() <= umbral;

        alertaInventarioRepository
                .findFirstByProductoIdAndEstadoIn(producto.getId(), ALERTAS_VIGENTES)
                .ifPresentOrElse(alerta -> actualizarAlertaExistente(alerta, producto, umbral, stockBajo),
                        () -> crearAlertaSiAplica(producto, umbral, stockBajo));
    }

    private void actualizarAlertaExistente(
            AlertaInventario alerta,
            Producto producto,
            int umbral,
            boolean stockBajo
    ) {
        if (stockBajo) {
            alerta.setStockActual(producto.getStock());
            alerta.setUmbral(umbral);
        } else {
            alerta.setStockActual(producto.getStock() != null ? producto.getStock() : 0);
            alerta.setUmbral(umbral);
            alerta.setEstado(EstadoAlertaInventario.RESUELTA);
        }
        alertaInventarioRepository.save(alerta);
    }

    private void crearAlertaSiAplica(Producto producto, int umbral, boolean stockBajo) {
        if (!stockBajo) {
            return;
        }

        AlertaInventario alerta = AlertaInventario.builder()
                .producto(producto)
                .stockActual(producto.getStock())
                .umbral(umbral)
                .estado(EstadoAlertaInventario.ACTIVA)
                .build();

        alertaInventarioRepository.save(alerta);
    }

    private int obtenerUmbral(Producto producto) {
        return producto.getStockMinimo() != null && producto.getStockMinimo() > 0
                ? producto.getStockMinimo()
                : 5;
    }

    private AlertaInventarioResponse toResponse(AlertaInventario alerta) {
        Producto producto = alerta.getProducto();
        return new AlertaInventarioResponse(
                alerta.getId(),
                producto.getId(),
                producto.getNombre(),
                producto.getImagenUrl(),
                alerta.getStockActual(),
                alerta.getUmbral(),
                alerta.getEstado().name(),
                alerta.getFechaCreacion(),
                alerta.getFechaActualizacion()
        );
    }
}
