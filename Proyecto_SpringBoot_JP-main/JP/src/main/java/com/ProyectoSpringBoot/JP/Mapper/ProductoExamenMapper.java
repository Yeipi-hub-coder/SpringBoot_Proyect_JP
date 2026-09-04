package com.ProyectoSpringBoot.JP.Mapper;

import com.ProyectoSpringBoot.JP.Model.Producto;
import com.ProyectoSpringBoot.JP.Model.ProductoExamen;
import com.ProyectoSpringBoot.JP.dto.request.ProductoExamenRequest;
import com.ProyectoSpringBoot.JP.dto.request.ProductoRequest;
import com.ProyectoSpringBoot.JP.dto.response.ProductoExamenResponse;
import com.ProyectoSpringBoot.JP.dto.response.ProductoResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductoExamenMapper {

    public ProductoExamen dtoToEntity(ProductoExamenRequest dto) {
        if (dto == null) return null;
        ProductoExamen producto = new ProductoExamen();
        producto.setNombre(dto.nombre());
        producto.setStock(dto.stock());
        return producto;
    }

    public ProductoExamenResponse entityToDto(ProductoExamen producto) {
        if (producto == null) return null;
        return new ProductoExamenResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getStock(),
                producto.getBodega()
        );
    }
}
