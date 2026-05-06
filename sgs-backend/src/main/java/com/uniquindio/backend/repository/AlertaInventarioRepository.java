package com.uniquindio.backend.repository;

import com.uniquindio.backend.model.AlertaInventario;
import com.uniquindio.backend.model.EstadoAlertaInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AlertaInventarioRepository extends JpaRepository<AlertaInventario, Long> {

    List<AlertaInventario> findByEstadoInOrderByFechaCreacionDesc(Collection<EstadoAlertaInventario> estados);

    Optional<AlertaInventario> findFirstByProductoIdAndEstadoIn(
            Long productoId,
            Collection<EstadoAlertaInventario> estados
    );

    long countByEstadoIn(Collection<EstadoAlertaInventario> estados);
}
