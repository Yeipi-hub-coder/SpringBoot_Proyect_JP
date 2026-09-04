package com.ProyectoSpringBoot.JP.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductoExamenRequest (
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String nombre,

        @Positive(message = "El stock no puede ser negativo")
        Integer stock,

        Long bodega
){
}
