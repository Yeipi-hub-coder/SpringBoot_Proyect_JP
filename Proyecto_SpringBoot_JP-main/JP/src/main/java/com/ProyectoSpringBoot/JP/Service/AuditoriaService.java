package com.ProyectoSpringBoot.JP.Service;

import com.ProyectoSpringBoot.JP.dto.response.AuditoriaResponse;

import java.util.List;

public interface AuditoriaService {

    AuditoriaResponse obtenerPorId(Long id);

    List<AuditoriaResponse> obtenerTodos();
}