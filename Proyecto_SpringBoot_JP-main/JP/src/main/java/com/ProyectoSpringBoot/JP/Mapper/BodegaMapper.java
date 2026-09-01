package com.ProyectoSpringBoot.JP.Mapper;

import com.ProyectoSpringBoot.JP.Model.Bodega;
import com.ProyectoSpringBoot.JP.dto.request.BodegaRequest;
import com.ProyectoSpringBoot.JP.dto.response.BodegaResponse;
import org.springframework.stereotype.Component;

@Component
public class BodegaMapper {
    public Bodega dtoToEntity(BodegaRequest dto) {
        if (dto == null) return null;
        Bodega bodega = new Bodega();
        bodega.setNombre(dto.nombre());
        bodega.setUbicacion(dto.ubicacion());
        bodega.setCapacidad(dto.capacidad());
        bodega.setEncargado(dto.encargado());
        return bodega;
    }

    public BodegaResponse entityToDto(Bodega bodega) {
        if(bodega == null) return null;
        return new BodegaResponse(
                bodega.getId(),
                bodega.getNombre(),
                bodega.getUbicacion(),
                bodega.getCapacidad(),
                bodega.getEncargado()
        );
    }
}
