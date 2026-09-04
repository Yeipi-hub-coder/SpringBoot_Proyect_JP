package com.ProyectoSpringBoot.JP.controller;

import com.ProyectoSpringBoot.JP.Service.ProductoExamenService;
import com.ProyectoSpringBoot.JP.dto.request.ProductoExamenRequest;
import com.ProyectoSpringBoot.JP.dto.response.ProductoExamenResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productosExamen")
public class ProductoExamenController {

    private final ProductoExamenService productoService;

    public ProductoExamenController(ProductoExamenService productoService) {this.productoService = productoService;}

    public ResponseEntity<ProductoExamenResponse> crear(
            @Valid @RequestBody ProductoExamenRequest dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoService.crear(dto));
    }

    public ResponseEntity<List<ProductoExamenResponse>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }
}
