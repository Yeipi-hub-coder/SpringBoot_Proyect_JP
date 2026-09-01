package com.ProyectoSpringBoot.JP.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BodegaRequest (
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    String nombre,

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 200, message = "La ubicación no puede superar los 200 caracteres")
    String ubicacion,

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    Integer capacidad,

    @NotBlank(message = "El encargado es obligatorio")
    @Size(max = 100, message = "El encargado no puede superar los 100 caracteres")
    String encargado){
}
