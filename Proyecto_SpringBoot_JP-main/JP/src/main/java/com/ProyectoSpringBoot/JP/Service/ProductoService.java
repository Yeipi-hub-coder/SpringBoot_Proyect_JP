package com.ProyectoSpringBoot.JP.Service;

import com.ProyectoSpringBoot.JP.dto.request.ProductoRequest;
import com.ProyectoSpringBoot.JP.dto.response.ProductoResponse;

import java.util.List;

public interface ProductoService {

    ProductoResponse crear(ProductoRequest dto);

    ProductoResponse obtenerPorId(Long id);

    List<ProductoResponse> obtenerTodos();

    ProductoResponse actualizar(Long id, ProductoRequest dto);

    void eliminar(Long id);
}