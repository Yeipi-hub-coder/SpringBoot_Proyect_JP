package com.ProyectoSpringBoot.JP.Service.impl;

import com.ProyectoSpringBoot.JP.Mapper.BodegaExamenMapper;
import com.ProyectoSpringBoot.JP.Model.BodegaExamen;
import com.ProyectoSpringBoot.JP.Repository.BodegaExamenRepository;
import com.ProyectoSpringBoot.JP.Service.BodegaExamenService;
import com.ProyectoSpringBoot.JP.dto.request.BodegaExamenRequest;
import com.ProyectoSpringBoot.JP.dto.response.BodegaExamenResponse;
import com.ProyectoSpringBoot.JP.dto.response.BodegaResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BodegaExamenServiceImpl implements BodegaExamenService {
    private final BodegaExamenRepository bodegaRepository;
    private final BodegaExamenMapper bodegaMapper;


    public BodegaExamenServiceImpl(
            BodegaExamenRepository bodegaRepository,
            BodegaExamenMapper bodegaMapper
            ) {
        this.bodegaRepository = bodegaRepository;
        this.bodegaMapper = bodegaMapper;
    }

    @Override
    public BodegaExamenResponse crear(BodegaExamenRequest dto) {

        BodegaExamen bodega = bodegaMapper.dtoToEntity(dto);

        BodegaExamen guardada = bodegaRepository.save(bodega);

        return bodegaMapper.entityToDto(guardada);
    }

    @Override
    public List<BodegaExamenResponse> obtenerTodos() {

        return bodegaRepository.findAll()
                .stream()
                .map(bodegaMapper::entityToDto)
                .toList();
    }
}
