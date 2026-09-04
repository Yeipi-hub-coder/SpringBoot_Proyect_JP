package com.ProyectoSpringBoot.JP.Service.impl;

import com.ProyectoSpringBoot.JP.Mapper.ProductoExamenMapper;
import com.ProyectoSpringBoot.JP.Model.ProductoExamen;
import com.ProyectoSpringBoot.JP.Repository.ProductoExamenRepository;
import com.ProyectoSpringBoot.JP.Service.ProductoExamenService;
import com.ProyectoSpringBoot.JP.dto.request.ProductoExamenRequest;
import com.ProyectoSpringBoot.JP.dto.response.ProductoExamenResponse;
import com.ProyectoSpringBoot.JP.dto.response.ProductoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoExamenServiceImpl implements ProductoExamenService {

    private final ProductoExamenMapper productoMapper;

    private final ProductoExamenRepository productoRepository;

    public ProductoExamenServiceImpl(
            ProductoExamenMapper productoMapper,
            ProductoExamenRepository productoRepository) {
        this.productoMapper = productoMapper;
        this.productoRepository = productoRepository;
    }

    @Override
    public ProductoExamenResponse crear(ProductoExamenRequest dto) {
        ProductoExamen producto = productoMapper.dtoToEntity(dto);

        ProductoExamen guardado = productoRepository.save(producto);

        return productoMapper.entityToDto(guardado);
    }

    @Override
    public List<ProductoExamenResponse> obtenerTodos() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::entityToDto)
                .toList();
    }
}
