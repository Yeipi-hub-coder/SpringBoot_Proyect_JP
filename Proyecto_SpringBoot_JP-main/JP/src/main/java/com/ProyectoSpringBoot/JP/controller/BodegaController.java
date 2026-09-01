package com.ProyectoSpringBoot.JP.controller;

import com.ProyectoSpringBoot.JP.dto.request.BodegaRequest;
import com.ProyectoSpringBoot.JP.dto.response.BodegaResponse;
import com.ProyectoSpringBoot.JP.Service.BodegaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bodegas")
@Tag(
        name = "Bodegas",
        description = "Operaciones para la gestión de bodegas"
)
@SecurityRequirement(name = "bearerAuth")
public class BodegaController {

    private final BodegaService bodegaService;

    public BodegaController(BodegaService bodegaService) {
        this.bodegaService = bodegaService;
    }

    @Operation(
            summary = "Crear una bodega",
            description = "Registra una nueva bodega en el sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Bodega creada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            )
    })
    @PostMapping
    public ResponseEntity<BodegaResponse> crear(
            @Valid @RequestBody BodegaRequest dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bodegaService.crear(dto));
    }

    @Operation(
            summary = "Obtener una bodega",
            description = "Busca una bodega por su identificador"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Bodega encontrada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bodega no encontrada"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponse> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(bodegaService.obtenerPorId(id));
    }

    @Operation(
            summary = "Listar bodegas",
            description = "Obtiene todas las bodegas registradas"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de bodegas obtenida correctamente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            )
    })
    @GetMapping
    public ResponseEntity<List<BodegaResponse>> obtenerTodos() {
        return ResponseEntity.ok(bodegaService.obtenerTodos());
    }

    @Operation(
            summary = "Actualizar una bodega",
            description = "Actualiza los datos de una bodega existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Bodega actualizada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bodega no encontrada"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<BodegaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody BodegaRequest dto
    ) {
        return ResponseEntity.ok(bodegaService.actualizar(id, dto));
    }

    @Operation(
            summary = "Eliminar una bodega",
            description = "Elimina una bodega por su identificador"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "403",
                    description = "La bodega no se puede eliminar por que está referenciada por otra tabla"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Bodega eliminada correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bodega no encontrada"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {
        bodegaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}