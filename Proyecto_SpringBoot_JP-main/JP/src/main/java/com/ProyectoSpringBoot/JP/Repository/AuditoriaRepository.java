package com.ProyectoSpringBoot.JP.Repository;

import com.ProyectoSpringBoot.JP.Model.Auditoria;
import com.ProyectoSpringBoot.JP.Model.Enums.TipoOperacionEnum;
import com.ProyectoSpringBoot.JP.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByUsuario(Usuario usuario);
    List<Auditoria> findByOperacion(TipoOperacionEnum operacion);
    List<Auditoria> findByFecha(Date fechaInicio);
}
