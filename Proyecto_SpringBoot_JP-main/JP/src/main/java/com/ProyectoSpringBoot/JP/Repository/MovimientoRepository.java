package com.ProyectoSpringBoot.JP.Repository;

import com.ProyectoSpringBoot.JP.Model.Enums.TipoMovimientoEnum;
import com.ProyectoSpringBoot.JP.Model.Movimiento;
import com.ProyectoSpringBoot.JP.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Movimiento> findByUsuario(Usuario usuario);
    List<Movimiento> findByTipo(TipoMovimientoEnum tipo);
}
