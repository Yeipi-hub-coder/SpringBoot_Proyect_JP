package com.ProyectoSpringBoot.JP.dto.response;

import com.ProyectoSpringBoot.JP.Model.Enums.TipoMovimientoEnum;

import java.time.LocalDateTime;
import java.util.List;

public record MovimientoResponse (
    Long id,
    LocalDateTime fecha,
    TipoMovimientoEnum tipo,
    Long usuarioId,
    String usuarioNombre,
    Long bodegaOrigenId,
    String bodegaOrigenNombre,
    Long bodegaDestinoId,
    String bodegaDestinoNombre,
    List<MovimientoDetalleResponse> detalles
    ){
}
