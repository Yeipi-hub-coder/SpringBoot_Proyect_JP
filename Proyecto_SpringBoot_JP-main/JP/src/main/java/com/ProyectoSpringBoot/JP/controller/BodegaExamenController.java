package com.ProyectoSpringBoot.JP.controller;

import com.ProyectoSpringBoot.JP.Service.BodegaExamenService;
import com.ProyectoSpringBoot.JP.dto.request.BodegaExamenRequest;
import com.ProyectoSpringBoot.JP.dto.response.BodegaExamenResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bodegasExamen")
public class BodegaExamenController {

    private final BodegaExamenService bodegaService;

    public BodegaExamenController(BodegaExamenService bodegaService) {this.bodegaService = bodegaService;}

    @PostMapping
    public ResponseEntity<BodegaExamenResponse> crear(
            @Valid @RequestBody BodegaExamenRequest dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bodegaService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<BodegaExamenResponse>> obtenerTodos() {
        return ResponseEntity.ok(bodegaService.obtenerTodos());
    }
}
