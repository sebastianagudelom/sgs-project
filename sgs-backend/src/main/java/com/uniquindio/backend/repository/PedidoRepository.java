package com.uniquindio.backend.repository;

import com.uniquindio.backend.model.Pedido;
import com.uniquindio.backend.model.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    List<Pedido> findByEstadoOrderByFechaCreacionDesc(EstadoPedido estado);

    List<Pedido> findAllByOrderByFechaCreacionDesc();

    long countByUsuarioId(Long usuarioId);

    List<Pedido> findByEstadoAndFechaCreacionBefore(EstadoPedido estado, LocalDateTime fechaLimite);

    List<Pedido> findByEstadoAndFechaUltimoCambioEstadoBefore(EstadoPedido estado, LocalDateTime fechaLimite);
}
