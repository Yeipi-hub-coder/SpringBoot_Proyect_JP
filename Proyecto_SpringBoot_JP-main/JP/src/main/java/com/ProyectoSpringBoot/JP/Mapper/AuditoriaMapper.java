package com.ProyectoSpringBoot.JP.Mapper;

import com.ProyectoSpringBoot.JP.Model.Auditoria;
import com.ProyectoSpringBoot.JP.dto.response.AuditoriaResponse;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {

    public AuditoriaResponse entityToDto(Auditoria auditoria) {
        if (auditoria == null) return null;

        return new AuditoriaResponse(
                auditoria.getId(),
                auditoria.getFecha(),
                auditoria.getOperacion(),
                auditoria.getUsuario() != null
                        ? auditoria.getUsuario().getId()
                        : null,
                auditoria.getUsuario() != null
                        ? auditoria.getUsuario().getNombre()
                        : null,
                auditoria.getEntidad(),
                auditoria.getValoresAnteriores(),
                auditoria.getValoresNuevos()
        );
    }
}