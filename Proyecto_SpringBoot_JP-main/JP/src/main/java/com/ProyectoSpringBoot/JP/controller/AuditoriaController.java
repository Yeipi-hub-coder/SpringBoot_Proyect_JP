package com.ProyectoSpringBoot.JP.controller;

import com.ProyectoSpringBoot.JP.dto.response.AuditoriaResponse;
import com.ProyectoSpringBoot.JP.Service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
@Tag(
        name = "Auditorías",
        description = "Consulta de los registros de auditoría generados por la base de datos"
)
@SecurityRequirement(name = "bearerAuth")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @Operation(
            summary = "Obtener una auditoría",
            description = "Busca un registro de auditoría por su identificador"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Registro de auditoría encontrado"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No tiene permisos para consultar auditorías"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Registro de auditoría no encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaResponse> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(auditoriaService.obtenerPorId(id));
    }


    @Operation(
            summary = "Listar auditorías",
            description = "Obtiene todos los registros de auditoría generados por el sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Registros de auditoría obtenidos correctamente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No tiene permisos para consultar auditorías"
            )
    })
    @GetMapping
    public ResponseEntity<List<AuditoriaResponse>> obtenerTodos() {
        return ResponseEntity.ok(auditoriaService.obtenerTodos());
    }
}
