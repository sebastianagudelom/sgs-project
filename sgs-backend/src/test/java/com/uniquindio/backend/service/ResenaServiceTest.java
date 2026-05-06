package com.uniquindio.backend.service;

import com.uniquindio.backend.dto.ResenaResumenResponse;
import com.uniquindio.backend.repository.DetallePedidoRepository;
import com.uniquindio.backend.repository.ProductoRepository;
import com.uniquindio.backend.repository.ResenaRepository;
import com.uniquindio.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private DetallePedidoRepository detallePedidoRepository;

    @InjectMocks
    private ResenaService resenaService;

    @Test
    void obtenerResumenRetornaPromedioYTotalDeResenasDelProducto() {
        Long productoId = 25L;
        when(productoRepository.existsById(productoId)).thenReturn(true);
        when(resenaRepository.obtenerPromedioPorProducto(productoId)).thenReturn(4.25);
        when(resenaRepository.countByProductoId(productoId)).thenReturn(8L);

        ResenaResumenResponse resumen = resenaService.obtenerResumen(productoId);

        assertEquals(4.25, resumen.promedio());
        assertEquals(8L, resumen.total());
        verify(productoRepository).existsById(productoId);
        verify(resenaRepository).obtenerPromedioPorProducto(productoId);
        verify(resenaRepository).countByProductoId(productoId);
    }
}
