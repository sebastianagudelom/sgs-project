package com.uniquindio.backend.service;

import com.uniquindio.backend.dto.ClienteResponse;
import com.uniquindio.backend.model.Pedido;
import com.uniquindio.backend.model.Rol;
import com.uniquindio.backend.model.Usuario;
import com.uniquindio.backend.repository.PedidoRepository;
import com.uniquindio.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void listarClientesCalculaTotalPedidosYTotalGastadoPorCliente() {
        LocalDateTime fechaRegistro = LocalDateTime.of(2026, 5, 5, 9, 30);
        Usuario clienteConPedidos = Usuario.builder()
                .id(1L)
                .nombre("Ana")
                .apellido("Gomez")
                .email("ana@test.com")
                .cedula("123")
                .telefono("3001112233")
                .activo(true)
                .rol(Rol.CLIENTE)
                .fechaCreacion(fechaRegistro)
                .build();
        Usuario clienteSinPedidos = Usuario.builder()
                .id(2L)
                .nombre("Luis")
                .apellido("Perez")
                .email("luis@test.com")
                .cedula("456")
                .telefono("3004445566")
                .activo(true)
                .rol(Rol.CLIENTE)
                .fechaCreacion(fechaRegistro)
                .build();

        when(usuarioRepository.findByRolOrderByFechaCreacionDesc(Rol.CLIENTE))
                .thenReturn(List.of(clienteConPedidos, clienteSinPedidos));
        when(pedidoRepository.findByUsuarioIdOrderByFechaCreacionDesc(1L))
                .thenReturn(List.of(pedidoConTotal("120000.50"), pedidoConTotal("79500.50")));
        when(pedidoRepository.findByUsuarioIdOrderByFechaCreacionDesc(2L))
                .thenReturn(List.of());

        List<ClienteResponse> clientes = adminService.listarClientes();

        assertEquals(2, clientes.size());
        assertEquals(2, clientes.get(0).totalPedidos());
        assertEquals(new BigDecimal("199501.00"), clientes.get(0).totalGastado());
        assertEquals(0, clientes.get(1).totalPedidos());
        assertEquals(BigDecimal.ZERO, clientes.get(1).totalGastado());
        verify(usuarioRepository).findByRolOrderByFechaCreacionDesc(Rol.CLIENTE);
        verify(pedidoRepository).findByUsuarioIdOrderByFechaCreacionDesc(1L);
        verify(pedidoRepository).findByUsuarioIdOrderByFechaCreacionDesc(2L);
    }

    private Pedido pedidoConTotal(String total) {
        return Pedido.builder()
                .total(new BigDecimal(total))
                .build();
    }
}
