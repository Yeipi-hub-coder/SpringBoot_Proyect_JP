package com.ProyectoSpringBoot.JP.dto.response;

import com.ProyectoSpringBoot.JP.Model.Enums.TipoOperacionEnum;

import java.util.Date;

public record AuditoriaResponse (
    Long id,
    Date fecha,
    TipoOperacionEnum operacion,
    Long usuarioId,
    String usuarioNombre,
    String entidad,
    String valoresAnteriores,
    String valoresNuevos
    ){
}
