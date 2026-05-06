package com.uniquindio.backend.repository;

import com.uniquindio.backend.model.DetallePedido;
import com.uniquindio.backend.model.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    boolean existsByProductoId(Long productoId);

    boolean existsByPedidoUsuarioIdAndProductoIdAndPedidoEstadoIn(
            Long usuarioId,
            Long productoId,
            Collection<EstadoPedido> estados
    );
}
