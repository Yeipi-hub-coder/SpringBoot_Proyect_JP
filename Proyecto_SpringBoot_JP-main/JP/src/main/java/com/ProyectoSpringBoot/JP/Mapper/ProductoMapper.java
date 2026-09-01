package com.ProyectoSpringBoot.JP.Mapper;

import com.ProyectoSpringBoot.JP.Model.Producto;
import com.ProyectoSpringBoot.JP.dto.request.ProductoRequest;
import com.ProyectoSpringBoot.JP.dto.response.ProductoResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto dtoToEntity(ProductoRequest dto) {
        if (dto == null) return null;
        Producto producto = new Producto();
        producto.setNombre(dto.nombre());
        producto.setCategoria(dto.categoria());
        producto.setPrecio(dto.precio());
        return producto;
    }

    public ProductoResponse entityToDto(Producto producto) {
        if (producto == null) return null;
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getPrecio()
        );
    }
}