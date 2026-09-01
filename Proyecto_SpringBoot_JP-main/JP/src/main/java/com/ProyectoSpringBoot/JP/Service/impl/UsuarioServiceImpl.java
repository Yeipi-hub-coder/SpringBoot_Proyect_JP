package com.ProyectoSpringBoot.JP.Service.impl;

import com.ProyectoSpringBoot.JP.Auth.UsuarioAutenticadoService;
import com.ProyectoSpringBoot.JP.Auth.UsuarioSesionService;
import com.ProyectoSpringBoot.JP.Model.Usuario;
import com.ProyectoSpringBoot.JP.dto.request.UsuarioRequest;
import com.ProyectoSpringBoot.JP.dto.response.UsuarioResponse;
import com.ProyectoSpringBoot.JP.exception.BadRequestException;
import com.ProyectoSpringBoot.JP.exception.ResourceNotFoundException;
import com.ProyectoSpringBoot.JP.Mapper.UsuarioMapper;
import com.ProyectoSpringBoot.JP.Repository.UsuarioRepository;
import com.ProyectoSpringBoot.JP.Service.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final UsuarioSesionService usuarioSesionService;

    public UsuarioServiceImpl(
            UsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper,
            PasswordEncoder passwordEncoder,
            UsuarioAutenticadoService usuarioAutenticadoService,
            UsuarioSesionService usuarioSesionService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
        this.usuarioSesionService = usuarioSesionService;
    }

    @Override
    public UsuarioResponse crear(UsuarioRequest dto) {

        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new BadRequestException(
                    "Ya existe un usuario con ese email"
            );
        }

        Usuario usuario = usuarioMapper.dtoToEntity(dto);

        usuario.setPassword(
                passwordEncoder.encode(dto.password())
        );

        Usuario guardado = usuarioRepository.save(usuario);

        return usuarioMapper.entityToDto(guardado);
    }

    @Override
    public UsuarioResponse obtenerPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        ));

        return usuarioMapper.entityToDto(usuario);
    }

    @Override
    public List<UsuarioResponse> obtenerTodos() {

        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioRequest dto) {

        Usuario usuarioAutenticado = usuarioAutenticadoService.obtenerUsuario();

        usuarioSesionService.establecerUsuario(usuarioAutenticado.getId());

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        ));

        if (!usuario.getEmail().equals(dto.email())
                && usuarioRepository.existsByEmail(dto.email())) {

            throw new BadRequestException(
                    "Ya existe un usuario con ese email"
            );
        }

        usuario.setNombre(dto.nombre());
        usuario.setEmail(dto.email());
        usuario.setPassword(
                passwordEncoder.encode(dto.password())
        );
        usuario.setRol(dto.rol());

        Usuario actualizado = usuarioRepository.save(usuario);

        String valoresAnteriores =
                "nombre=" + usuario.getNombre()
                        + ", email=" + usuario.getEmail()
                        + ", rol=" + usuario.getRol();

        String valoresNuevos =
                "nombre=" + actualizado.getNombre()
                        + ", email=" + actualizado.getEmail()
                        + ", rol=" + actualizado.getRol();


        return usuarioMapper.entityToDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Usuario usuarioAutenticado = usuarioAutenticadoService.obtenerUsuario();

        usuarioSesionService.establecerUsuario(usuarioAutenticado.getId());


        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        ));

        String valoresAnteriores =
                "id=" + usuario.getId()
                        + ", nombre=" + usuario.getNombre()
                        + ", email=" + usuario.getEmail()
                        + ", rol=" + usuario.getRol();

        usuarioRepository.delete(usuario);
    }
}