package com.ProyectoSpringBoot.JP.Service.impl;

import com.ProyectoSpringBoot.JP.Auth.UsuarioAutenticadoService;
import com.ProyectoSpringBoot.JP.Auth.UsuarioSesionService;
import com.ProyectoSpringBoot.JP.Model.Enums.TipoOperacionEnum;
import com.ProyectoSpringBoot.JP.Model.Producto;
import com.ProyectoSpringBoot.JP.Model.Usuario;
import com.ProyectoSpringBoot.JP.Service.AuditoriaService;
import com.ProyectoSpringBoot.JP.dto.request.ProductoRequest;
import com.ProyectoSpringBoot.JP.dto.response.ProductoResponse;
import com.ProyectoSpringBoot.JP.exception.ResourceNotFoundException;
import com.ProyectoSpringBoot.JP.Mapper.ProductoMapper;
import com.ProyectoSpringBoot.JP.Repository.ProductoRepository;
import com.ProyectoSpringBoot.JP.Service.ProductoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final UsuarioSesionService usuarioSesionService;
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    public ProductoServiceImpl(
            ProductoRepository productoRepository,
            ProductoMapper productoMapper,
            UsuarioAutenticadoService usuarioAutenticadoService,
            UsuarioSesionService usuarioSesionService
    ) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
        this.usuarioSesionService = usuarioSesionService;
    }

    @Override
    @Transactional
    public ProductoResponse crear(ProductoRequest dto) {

        Usuario usuario = usuarioAutenticadoService.obtenerUsuario();

        usuarioSesionService.establecerUsuario(usuario.getId());

        Producto producto = productoMapper.dtoToEntity(dto);

        Producto guardado = productoRepository.save(producto);

        return productoMapper.entityToDto(guardado);
    }

    @Override
    public ProductoResponse obtenerPorId(Long id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        return productoMapper.entityToDto(producto);
    }

    @Override
    public List<ProductoResponse> obtenerTodos() {

        return productoRepository.findAll()
                .stream()
                .map(productoMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest dto) {

        Usuario usuario = usuarioAutenticadoService.obtenerUsuario();

        usuarioSesionService.establecerUsuario(usuario.getId());

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Producto no encontrado"
                        ));

        String valoresAnteriores =
                "nombre=" + producto.getNombre()
                        + ", categoria=" + producto.getCategoria()
                        + ", precio=" + producto.getPrecio();

        producto.setNombre(dto.nombre());
        producto.setCategoria(dto.categoria());
        producto.setPrecio(dto.precio());

        Producto actualizado = productoRepository.save(producto);

        String valoresNuevos =
                "nombre=" + actualizado.getNombre()
                        + ", categoria=" + actualizado.getCategoria()
                        + ", precio=" + actualizado.getPrecio();

        return productoMapper.entityToDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Usuario usuario = usuarioAutenticadoService.obtenerUsuario();

        usuarioSesionService.establecerUsuario(usuario.getId());

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Producto no encontrado"
                        ));

        String valoresAnteriores =
                "id=" + producto.getId()
                        + ", nombre=" + producto.getNombre()
                        + ", categoria=" + producto.getCategoria()
                        + ", precio=" + producto.getPrecio();

        productoRepository.delete(producto);
    }
}
