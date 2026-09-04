package com.ProyectoSpringBoot.JP.Service;

import com.ProyectoSpringBoot.JP.dto.request.ProductoExamenRequest;
import com.ProyectoSpringBoot.JP.dto.request.ProductoRequest;
import com.ProyectoSpringBoot.JP.dto.response.ProductoExamenResponse;
import com.ProyectoSpringBoot.JP.dto.response.ProductoResponse;

import java.util.List;

public interface ProductoExamenService {

    ProductoExamenResponse crear(ProductoExamenRequest dto);

    List<ProductoExamenResponse> obtenerTodos();
}
