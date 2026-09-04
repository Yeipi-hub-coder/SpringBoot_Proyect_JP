package com.ProyectoSpringBoot.JP.Mapper;

import com.ProyectoSpringBoot.JP.Model.Bodega;
import com.ProyectoSpringBoot.JP.Model.BodegaExamen;
import com.ProyectoSpringBoot.JP.dto.request.BodegaExamenRequest;
import com.ProyectoSpringBoot.JP.dto.response.BodegaExamenResponse;
import com.ProyectoSpringBoot.JP.dto.response.BodegaResponse;
import org.springframework.stereotype.Component;

@Component
public class BodegaExamenMapper {

    public BodegaExamen dtoToEntity(BodegaExamenRequest dto) {
        System.out.println(dto);
        if (dto == null) return null;
        BodegaExamen bodega = new BodegaExamen();
        bodega.setNombre(dto.nombre());
        bodega.setUbicacion(dto.ubicacion());
        return bodega;
    }

    public BodegaExamenResponse entityToDto(BodegaExamen bodega) {
        if(bodega == null) return null;
        return new BodegaExamenResponse(
                bodega.getId(),
                bodega.getNombre(),
                bodega.getUbicacion()
        );
    }
}
