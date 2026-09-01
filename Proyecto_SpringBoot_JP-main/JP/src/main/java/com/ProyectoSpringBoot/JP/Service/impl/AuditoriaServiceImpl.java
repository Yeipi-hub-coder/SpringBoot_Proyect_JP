package com.ProyectoSpringBoot.JP.Service.impl;

import com.ProyectoSpringBoot.JP.Model.Auditoria;
import com.ProyectoSpringBoot.JP.dto.response.AuditoriaResponse;
import com.ProyectoSpringBoot.JP.exception.ResourceNotFoundException;
import com.ProyectoSpringBoot.JP.Mapper.AuditoriaMapper;
import com.ProyectoSpringBoot.JP.Repository.AuditoriaRepository;
import com.ProyectoSpringBoot.JP.Service.AuditoriaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;

    public AuditoriaServiceImpl(
            AuditoriaRepository auditoriaRepository,
            AuditoriaMapper auditoriaMapper
    ) {
        this.auditoriaRepository = auditoriaRepository;
        this.auditoriaMapper = auditoriaMapper;
    }

    @Override
    public AuditoriaResponse obtenerPorId(Long id) {

        Auditoria auditoria = auditoriaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Auditoría no encontrada"
                        ));

        return auditoriaMapper.entityToDto(auditoria);
    }

    @Override
    public List<AuditoriaResponse> obtenerTodos() {

        return auditoriaRepository.findAll()
                .stream()
                .map(auditoriaMapper::entityToDto)
                .toList();
    }
}