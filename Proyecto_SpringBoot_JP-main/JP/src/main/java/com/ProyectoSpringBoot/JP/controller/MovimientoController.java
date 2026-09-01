package com.ProyectoSpringBoot.JP.controller;

import com.ProyectoSpringBoot.JP.dto.request.MovimientoRequest;
import com.ProyectoSpringBoot.JP.dto.response.MovimientoResponse;
import com.ProyectoSpringBoot.JP.Service.MovimientoService;
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
@RequestMapping("/api/movimientos")
@Tag(
        name = "Movimientos",
        description = "Operaciones de entradas, salidas y transferencias de inventario"
)
@SecurityRequirement(name = "bearerAuth")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @Operation(
            summary = "Registrar un movimiento",
            description = """
                    Registra un movimiento de inventario.
                    
                    ENTRADA:
                    requiere bodegaDestinoId y aumenta el stock.
                    
                    SALIDA:
                    requiere bodegaOrigenId y disminuye el stock.
                    
                    TRANSFERENCIA:
                    requiere bodegaOrigenId y bodegaDestinoId y mueve stock entre ambas bodegas.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Movimiento registrado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos, bodega incorrecta o stock insuficiente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bodega o producto no encontrado"
            )
    })
    @PostMapping
    public ResponseEntity<MovimientoResponse> crear(
            @Valid @RequestBody MovimientoRequest dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(movimientoService.crear(dto));
    }

    @Operation(
            summary = "Obtener un movimiento",
            description = "Busca un movimiento por su identificador"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Movimiento encontrado"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Movimiento no encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(movimientoService.obtenerPorId(id));
    }

    @Operation(
            summary = "Listar movimientos",
            description = "Obtiene todos los movimientos registrados"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de movimientos obtenida correctamente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            )
    })
    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> obtenerTodos() {
        return ResponseEntity.ok(movimientoService.obtenerTodos());
    }

}
