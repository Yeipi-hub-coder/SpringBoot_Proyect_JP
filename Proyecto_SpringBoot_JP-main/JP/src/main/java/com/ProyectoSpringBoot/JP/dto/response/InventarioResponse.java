package com.ProyectoSpringBoot.JP.dto.response;

public record InventarioResponse (
    Long id,
    Long bodegaId,
    String bodegaNombre,
    Long productoId,
    String productoNombre,
    Integer stock
    ){
}
