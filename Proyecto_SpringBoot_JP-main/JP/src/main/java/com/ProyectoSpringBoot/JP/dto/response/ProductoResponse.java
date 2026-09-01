package com.ProyectoSpringBoot.JP.dto.response;

import java.math.BigDecimal;

public record ProductoResponse (
    Long id,
    String nombre,
    String categoria,
    BigDecimal precio
    ){
}
