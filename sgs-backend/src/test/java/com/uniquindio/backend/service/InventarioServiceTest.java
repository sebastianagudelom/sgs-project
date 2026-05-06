package com.uniquindio.backend.service;

import com.uniquindio.backend.model.AlertaInventario;
import com.uniquindio.backend.model.EstadoAlertaInventario;
import com.uniquindio.backend.model.Producto;
import com.uniquindio.backend.repository.AlertaInventarioRepository;
import com.uniquindio.backend.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private AlertaInventarioRepository alertaInventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void contarAlertasActivasUsaSoloEstadoActiva() {
        when(alertaInventarioRepository.countByEstadoIn(List.of(EstadoAlertaInventario.ACTIVA)))
                .thenReturn(4L);

        long total = inventarioService.contarAlertasActivas();

        assertEquals(4L, total);
        verify(alertaInventarioRepository).countByEstadoIn(List.of(EstadoAlertaInventario.ACTIVA));
    }

    @Test
    void sincronizarProductoCreaAlertaActivaCuandoStockEstaBajoElUmbral() {
        Producto producto = producto(10L, 3, 5, true);
        when(alertaInventarioRepository.findFirstByProductoIdAndEstadoIn(eq(10L), any()))
                .thenReturn(Optional.empty());
        ArgumentCaptor<AlertaInventario> alertaCaptor = ArgumentCaptor.forClass(AlertaInventario.class);

        inventarioService.sincronizarProducto(producto);

        verify(alertaInventarioRepository).save(alertaCaptor.capture());
        AlertaInventario alertaGuardada = alertaCaptor.getValue();
        assertSame(producto, alertaGuardada.getProducto());
        assertEquals(3, alertaGuardada.getStockActual());
        assertEquals(5, alertaGuardada.getUmbral());
        assertEquals(EstadoAlertaInventario.ACTIVA, alertaGuardada.getEstado());
    }

    @Test
    void sincronizarProductoResuelveAlertaVigenteCuandoStockSuperaElUmbral() {
        Producto producto = producto(10L, 12, 5, true);
        AlertaInventario alerta = AlertaInventario.builder()
                .producto(producto)
                .stockActual(3)
                .umbral(5)
                .estado(EstadoAlertaInventario.ACTIVA)
                .build();
        when(alertaInventarioRepository.findFirstByProductoIdAndEstadoIn(eq(10L), any()))
                .thenReturn(Optional.of(alerta));

        inventarioService.sincronizarProducto(producto);

        assertEquals(EstadoAlertaInventario.RESUELTA, alerta.getEstado());
        assertEquals(12, alerta.getStockActual());
        verify(alertaInventarioRepository).save(alerta);
    }

    private Producto producto(Long id, Integer stock, Integer stockMinimo, boolean activo) {
        return Producto.builder()
                .id(id)
                .nombre("Producto prueba")
                .stock(stock)
                .stockMinimo(stockMinimo)
                .activo(activo)
                .build();
    }
}
