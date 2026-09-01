package com.ProyectoSpringBoot.JP.Mapper;

import com.ProyectoSpringBoot.JP.Model.Inventario;
import com.ProyectoSpringBoot.JP.dto.request.InventarioRequest;
import com.ProyectoSpringBoot.JP.dto.response.InventarioResponse;
import org.springframework.stereotype.Component;

@Component
public class InventarioMapper {

    public Inventario dtoToEntity(InventarioRequest dto) {
        if (dto == null) return null;
        Inventario inventario = new Inventario();
        inventario.setStock(dto.stock());
        return inventario;
    }

    public InventarioResponse entityToDto(Inventario inventario) {
        if (inventario == null) return null;

        return new InventarioResponse(
                inventario.getId(),
                inventario.getBodega().getId(),
                inventario.getBodega().getNombre(),
                inventario.getProducto().getId(),
                inventario.getProducto().getNombre(),
                inventario.getStock()
        );
    }
}