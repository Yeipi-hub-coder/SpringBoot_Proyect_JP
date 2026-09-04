package com.ProyectoSpringBoot.JP.Service;

import com.ProyectoSpringBoot.JP.dto.request.BodegaExamenRequest;
import com.ProyectoSpringBoot.JP.dto.response.BodegaExamenResponse;

import java.util.List;

public interface BodegaExamenService {
    BodegaExamenResponse crear(BodegaExamenRequest dto);

    List<BodegaExamenResponse> obtenerTodos();
}
