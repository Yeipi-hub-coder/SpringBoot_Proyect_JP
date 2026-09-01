package com.ProyectoSpringBoot.JP.Repository;

import com.ProyectoSpringBoot.JP.Model.Bodega;
import com.ProyectoSpringBoot.JP.Model.Inventario;
import com.ProyectoSpringBoot.JP.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByBodegaAndProducto(Bodega bodega, Producto producto);

    Optional<Inventario> findByBodegaIdAndProductoId(Long bodegaId,Long productoId);
    List<Inventario> findByBodega(Bodega bodega);
    List<Inventario> findByStockLessThan(Integer stock);
}
