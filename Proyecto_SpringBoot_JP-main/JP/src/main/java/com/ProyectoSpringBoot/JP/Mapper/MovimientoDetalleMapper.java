package com.ProyectoSpringBoot.JP.Mapper;


import com.ProyectoSpringBoot.JP.Model.MovimientoDetalle;
import com.ProyectoSpringBoot.JP.dto.request.MovimientoDetalleRequest;
import com.ProyectoSpringBoot.JP.dto.response.MovimientoDetalleResponse;
import org.springframework.stereotype.Component;

@Component
public class MovimientoDetalleMapper {

    public MovimientoDetalle dtoToEntity(MovimientoDetalleRequest dto) {
        if (dto == null) return null;
        MovimientoDetalle detalle = new MovimientoDetalle();
        detalle.setCantidad(dto.cantidad());
        return detalle;
    }

    public MovimientoDetalleResponse entityToDto(MovimientoDetalle detalle) {
        if (detalle == null) return null;
        return new MovimientoDetalleResponse(
                detalle.getId(),
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad()
        );
    }
}