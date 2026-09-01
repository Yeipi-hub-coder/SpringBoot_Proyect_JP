package com.ProyectoSpringBoot.JP.controller;

import com.ProyectoSpringBoot.JP.dto.request.InventarioRequest;
import com.ProyectoSpringBoot.JP.dto.response.InventarioResponse;
import com.ProyectoSpringBoot.JP.Service.InventarioService;
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
@RequestMapping("/api/inventarios")
@Tag(
        name = "Inventarios",
        description = "Operaciones para la gestión del inventario de las bodegas"
)
@SecurityRequirement(name = "bearerAuth")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Operation(
            summary = "Crear un inventario",
            description = "Registra un producto dentro del inventario de una bodega"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Inventario creado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o el inventario ya existe"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            )
    })
    @PostMapping
    public ResponseEntity<InventarioResponse> crear(
            @Valid @RequestBody InventarioRequest dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inventarioService.crear(dto));
    }

    @Operation(
            summary = "Obtener un inventario",
            description = "Busca un registro de inventario por su identificador"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponse> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(inventarioService.obtenerPorId(id));
    }

    @Operation(
            summary = "Listar inventarios",
            description = "Obtiene todos los registros de inventario"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de inventarios obtenida correctamente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            )
    })
    @GetMapping
    public ResponseEntity<List<InventarioResponse>> obtenerTodos() {
        return ResponseEntity.ok(inventarioService.obtenerTodos());
    }

    @Operation(
            summary = "Actualizar un inventario",
            description = "Actualiza la bodega, producto o stock de un registro de inventario"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario actualizado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o conflicto de inventario"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario, bodega o producto no encontrado"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioRequest dto
    ) {
        return ResponseEntity.ok(inventarioService.actualizar(id, dto));
    }

    @Operation(
            summary = "Eliminar un inventario",
            description = "Elimina un registro de inventario por su identificador"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Inventario eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado"
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
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
