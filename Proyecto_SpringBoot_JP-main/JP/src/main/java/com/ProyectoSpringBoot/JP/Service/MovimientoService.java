package com.ProyectoSpringBoot.JP.Service;


import com.ProyectoSpringBoot.JP.dto.request.MovimientoRequest;
import com.ProyectoSpringBoot.JP.dto.response.MovimientoResponse;

import java.util.List;

public interface MovimientoService {

    MovimientoResponse crear(MovimientoRequest dto);

    MovimientoResponse obtenerPorId(Long id);

    List<MovimientoResponse> obtenerTodos();
}