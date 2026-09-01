package com.ProyectoSpringBoot.JP.Service;

import com.ProyectoSpringBoot.JP.dto.request.BodegaRequest;
import com.ProyectoSpringBoot.JP.dto.response.BodegaResponse;

import java.util.List;

public interface BodegaService {

    BodegaResponse crear(BodegaRequest dto);

    BodegaResponse obtenerPorId(Long id);

    List<BodegaResponse> obtenerTodos();

    BodegaResponse actualizar(Long id, BodegaRequest dto);

    void eliminar(Long id);
}
