package com.ProyectoSpringBoot.JP.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventarioRequest(

        @NotNull(message = "La bodega es obligatoria")
        Long bodegaId,

        @NotNull(message = "El producto es obligatorio")
        Long productoId,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock

) {
}
