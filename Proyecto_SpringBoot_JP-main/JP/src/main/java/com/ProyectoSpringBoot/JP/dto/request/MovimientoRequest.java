package com.ProyectoSpringBoot.JP.dto.request;

import com.ProyectoSpringBoot.JP.Model.Enums.TipoMovimientoEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MovimientoRequest (

    @NotNull(message = "El tipo de movimiento es obligatorio")
    TipoMovimientoEnum tipo,

    Long bodegaOrigenId,

    Long bodegaDestinoId,

    @NotEmpty(message = "Debe existir al menos un producto")
    List<MovimientoDetalleRequest> detalles
    ){
}
