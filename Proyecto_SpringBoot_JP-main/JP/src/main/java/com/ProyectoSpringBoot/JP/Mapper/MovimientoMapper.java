package com.ProyectoSpringBoot.JP.Mapper;

import com.ProyectoSpringBoot.JP.Model.Movimiento;
import com.ProyectoSpringBoot.JP.dto.request.MovimientoRequest;
import com.ProyectoSpringBoot.JP.dto.response.MovimientoResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MovimientoMapper {

    public Movimiento dtoToEntity(MovimientoRequest dto) {
        if (dto == null) return null;

        Movimiento movimiento = new Movimiento();

        movimiento.setTipo(dto.tipo());

        return movimiento;
    }

    public MovimientoResponse entityToDto(Movimiento movimiento) {
        if (movimiento == null) return null;

        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getFecha(),
                movimiento.getTipo(),
                movimiento.getUsuario().getId(),
                movimiento.getUsuario().getNombre(),
                movimiento.getBodegaOrigen() != null
                        ? movimiento.getBodegaOrigen().getId()
                        : null,
                movimiento.getBodegaOrigen() != null
                        ? movimiento.getBodegaOrigen().getNombre()
                        : null,
                movimiento.getBodegaDestino() != null
                        ? movimiento.getBodegaDestino().getId()
                        : null,
                movimiento.getBodegaDestino() != null
                        ? movimiento.getBodegaDestino().getNombre()
                        : null,
                movimiento.getDetalles() != null
                        ? movimiento.getDetalles()
                        .stream()
                        .map(detalle -> new com.ProyectoSpringBoot.JP.dto.response.MovimientoDetalleResponse(
                                detalle.getId(),
                                detalle.getProducto().getId(),
                                detalle.getProducto().getNombre(),
                                detalle.getCantidad()
                        ))
                        .collect(Collectors.toList())
                        : null
        );
    }
}
