package com.ProyectoSpringBoot.JP.dto.response;

public record BodegaResponse (
    Long id,
    String nombre,
    String ubicacion,
    Integer capacidad,
    String encargado
    ){
}
