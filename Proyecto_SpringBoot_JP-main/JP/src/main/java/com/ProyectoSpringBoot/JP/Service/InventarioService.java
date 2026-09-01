package com.ProyectoSpringBoot.JP.Service;

import com.ProyectoSpringBoot.JP.dto.request.InventarioRequest;
import com.ProyectoSpringBoot.JP.dto.response.InventarioResponse;

import java.util.List;

public interface InventarioService {

    InventarioResponse crear(InventarioRequest dto);

    InventarioResponse obtenerPorId(Long id);

    List<InventarioResponse> obtenerTodos();

    InventarioResponse actualizar(Long id, InventarioRequest dto);

    void eliminar(Long id);
}