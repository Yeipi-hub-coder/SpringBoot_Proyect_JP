package com.ProyectoSpringBoot.JP.Mapper;

import com.ProyectoSpringBoot.JP.Model.Usuario;
import com.ProyectoSpringBoot.JP.dto.request.UsuarioRequest;
import com.ProyectoSpringBoot.JP.dto.response.UsuarioResponse;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario dtoToEntity(UsuarioRequest dto) {
        if (dto == null) return null;
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setEmail(dto.email());
        usuario.setPassword(dto.password());
        usuario.setRol(dto.rol());
        return usuario;
    }

    public UsuarioResponse entityToDto(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }
}
