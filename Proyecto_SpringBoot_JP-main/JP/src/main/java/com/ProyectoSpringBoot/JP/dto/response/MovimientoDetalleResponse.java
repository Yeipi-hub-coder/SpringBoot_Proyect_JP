package com.ProyectoSpringBoot.JP.dto.response;

public record MovimientoDetalleResponse (
    Long id,
    Long productoId,
    String productoNombre,
    Integer cantidad
    ){
}
