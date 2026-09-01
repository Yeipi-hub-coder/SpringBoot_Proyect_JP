package com.ProyectoSpringBoot.JP.Auth;

public record LoginResponse(

        String token,
        Long usuarioId,
        String nombre,
        String email

) {
}
