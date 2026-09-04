package com.ProyectoSpringBoot.JP.dto.response;

import com.ProyectoSpringBoot.JP.Model.BodegaExamen;

public record ProductoExamenResponse(
        Long id,
        String nombre,
        Integer Stock,
        BodegaExamen bodega
) {
}
