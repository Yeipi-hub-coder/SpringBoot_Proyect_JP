package com.ProyectoSpringBoot.JP.Repository;

import com.ProyectoSpringBoot.JP.Model.MovimientoDetalle;
import com.ProyectoSpringBoot.JP.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoDetalleRepository extends JpaRepository<MovimientoDetalle, Long> {
    List<MovimientoDetalle> findByProducto(Producto producto);
}
