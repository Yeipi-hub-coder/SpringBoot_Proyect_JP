package com.ProyectoSpringBoot.JP.dto.response;

import com.ProyectoSpringBoot.JP.Model.Enums.RolEnum;

public record UsuarioResponse (
    Long id,
    String nombre,
    String email,
    RolEnum rol
    ){
}
