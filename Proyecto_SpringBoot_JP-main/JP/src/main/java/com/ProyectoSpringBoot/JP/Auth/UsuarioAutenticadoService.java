package com.ProyectoSpringBoot.JP.Auth;

import com.ProyectoSpringBoot.JP.Model.Usuario;
import com.ProyectoSpringBoot.JP.exception.BadRequestException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioAutenticadoService {

    public Usuario obtenerUsuario() {

        if (SecurityContextHolder.getContext()
                .getAuthentication() == null) {

            throw new BadRequestException(
                    "No hay un usuario autenticado"
            );
        }

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof Usuario)) {

            throw new BadRequestException(
                    "No se pudo identificar al usuario autenticado"
            );
        }

        return (Usuario) principal;
    }
}