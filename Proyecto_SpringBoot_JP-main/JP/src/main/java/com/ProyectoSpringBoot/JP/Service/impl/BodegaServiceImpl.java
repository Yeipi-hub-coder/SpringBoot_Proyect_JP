package com.ProyectoSpringBoot.JP.Service.impl;

import com.ProyectoSpringBoot.JP.Auth.UsuarioAutenticadoService;
import com.ProyectoSpringBoot.JP.Auth.UsuarioSesionService;
import com.ProyectoSpringBoot.JP.Model.Bodega;
import com.ProyectoSpringBoot.JP.Model.Usuario;
import com.ProyectoSpringBoot.JP.dto.request.BodegaRequest;
import com.ProyectoSpringBoot.JP.dto.response.BodegaResponse;
import com.ProyectoSpringBoot.JP.exception.ResourceNotFoundException;
import com.ProyectoSpringBoot.JP.Mapper.BodegaMapper;
import com.ProyectoSpringBoot.JP.Repository.BodegaRepository;
import com.ProyectoSpringBoot.JP.Service.BodegaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BodegaServiceImpl implements BodegaService {

    private final UsuarioSesionService usuarioSesionService;
    private final BodegaRepository bodegaRepository;
    private final BodegaMapper bodegaMapper;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public BodegaServiceImpl(
            BodegaRepository bodegaRepository,
            BodegaMapper bodegaMapper,
            UsuarioSesionService usuarioSesionService,
            UsuarioAutenticadoService usuarioAutenticadoService) {
        this.bodegaRepository = bodegaRepository;
        this.bodegaMapper = bodegaMapper;
        this.usuarioSesionService = usuarioSesionService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Override
    @Transactional
    public BodegaResponse crear(BodegaRequest dto) {
        Usuario usuario = usuarioAutenticadoService.obtenerUsuario();

        usuarioSesionService.establecerUsuario(usuario.getId());

        Bodega bodega = bodegaMapper.dtoToEntity(dto);

        Bodega guardada = bodegaRepository.save(bodega);

        return bodegaMapper.entityToDto(guardada);
    }

    @Override
    public BodegaResponse obtenerPorId(Long id) {

        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada"));

        return bodegaMapper.entityToDto(bodega);
    }

    @Override
    public List<BodegaResponse> obtenerTodos() {

        return bodegaRepository.findAll()
                .stream()
                .map(bodegaMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public BodegaResponse actualizar(Long id, BodegaRequest dto) {

        Usuario usuario = usuarioAutenticadoService.obtenerUsuario();

        usuarioSesionService.establecerUsuario(usuario.getId());

        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bodega no encontrada"
                        ));

        bodega.setNombre(dto.nombre());
        bodega.setUbicacion(dto.ubicacion());
        bodega.setCapacidad(dto.capacidad());
        bodega.setEncargado(dto.encargado());

        Bodega actualizada = bodegaRepository.save(bodega);

        return bodegaMapper.entityToDto(actualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Usuario usuario = usuarioAutenticadoService.obtenerUsuario();

        usuarioSesionService.establecerUsuario(usuario.getId());

        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bodega no encontrada"
                        ));

        bodegaRepository.delete(bodega);
    }
}