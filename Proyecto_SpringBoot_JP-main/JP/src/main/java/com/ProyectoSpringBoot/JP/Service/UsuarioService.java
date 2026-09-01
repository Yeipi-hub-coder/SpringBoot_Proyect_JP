package com.ProyectoSpringBoot.JP.Service;

import com.ProyectoSpringBoot.JP.dto.request.UsuarioRequest;
import com.ProyectoSpringBoot.JP.dto.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService {

    UsuarioResponse crear(UsuarioRequest dto);

    UsuarioResponse obtenerPorId(Long id);

    List<UsuarioResponse> obtenerTodos();

    UsuarioResponse actualizar(Long id, UsuarioRequest dto);

    void eliminar(Long id);
}