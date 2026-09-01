package com.ProyectoSpringBoot.JP.Service.impl;

import com.ProyectoSpringBoot.JP.Auth.UsuarioAutenticadoService;
import com.ProyectoSpringBoot.JP.Auth.UsuarioSesionService;
import com.ProyectoSpringBoot.JP.Model.Bodega;
import com.ProyectoSpringBoot.JP.Model.Inventario;
import com.ProyectoSpringBoot.JP.Model.Producto;
import com.ProyectoSpringBoot.JP.Model.Usuario;
import com.ProyectoSpringBoot.JP.dto.request.InventarioRequest;
import com.ProyectoSpringBoot.JP.dto.response.InventarioResponse;
import com.ProyectoSpringBoot.JP.exception.BadRequestException;
import com.ProyectoSpringBoot.JP.exception.ResourceNotFoundException;
import com.ProyectoSpringBoot.JP.Mapper.InventarioMapper;
import com.ProyectoSpringBoot.JP.Repository.BodegaRepository;
import com.ProyectoSpringBoot.JP.Repository.InventarioRepository;
import com.ProyectoSpringBoot.JP.Repository.ProductoRepository;
import com.ProyectoSpringBoot.JP.Service.InventarioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final InventarioMapper inventarioMapper;
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final UsuarioSesionService usuarioSesionService;

    public InventarioServiceImpl(
            InventarioRepository inventarioRepository,
            InventarioMapper inventarioMapper,
            BodegaRepository bodegaRepository,
            ProductoRepository productoRepository,
            UsuarioAutenticadoService usuarioAutenticadoService,
            UsuarioSesionService usuarioSesionService
    ) {
        this.inventarioRepository = inventarioRepository;
        this.inventarioMapper = inventarioMapper;
        this.bodegaRepository = bodegaRepository;
        this.productoRepository = productoRepository;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
        this.usuarioSesionService = usuarioSesionService;
    }

    @Override
    @Transactional
    public InventarioResponse crear(InventarioRequest dto) {

        Usuario usuario = usuarioAutenticadoService.obtenerUsuario();

        usuarioSesionService.establecerUsuario(usuario.getId());

        Inventario inventario = inventarioMapper.dtoToEntity(dto);

        Bodega bodega = bodegaRepository.findById(dto.bodegaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bodega no encontrada"));

        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado"));

        if (inventarioRepository.findByBodegaIdAndProductoId(
                dto.bodegaId(), dto.productoId()).isPresent()) {

            throw new BadRequestException(
                    "Ya existe inventario para ese producto en esa bodega"
            );
        }

        inventario.setBodega(bodega);
        inventario.setProducto(producto);

        Inventario guardado = inventarioRepository.save(inventario);

        return inventarioMapper.entityToDto(guardado);
    }

    @Override
    public InventarioResponse obtenerPorId(Long id) {

        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado"));

        return inventarioMapper.entityToDto(inventario);
    }

    @Override
    public List<InventarioResponse> obtenerTodos() {

        return inventarioRepository.findAll()
                .stream()
                .map(inventarioMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public InventarioResponse actualizar(Long id, InventarioRequest dto) {

        Usuario usuario = usuarioAutenticadoService.obtenerUsuario();

        usuarioSesionService.establecerUsuario(usuario.getId());

        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventario no encontrado"));

        Bodega bodega = bodegaRepository.findById(dto.bodegaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bodega no encontrada"));

        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado"));

        String valoresAnteriores =
                "bodega=" + inventario.getBodega().getId()
                        + ", producto=" + inventario.getProducto().getId()
                        + ", stock=" + inventario.getStock();

        if (!inventario.getBodega().getId().equals(dto.bodegaId())
                || !inventario.getProducto().getId().equals(dto.productoId())) {

            if (inventarioRepository.findByBodegaIdAndProductoId(
                    dto.bodegaId(), dto.productoId()).isPresent()) {

                throw new BadRequestException(
                        "Ya existe inventario para ese producto en esa bodega"
                );
            }
        }

        inventario.setBodega(bodega);
        inventario.setProducto(producto);
        inventario.setStock(dto.stock());

        Inventario actualizado = inventarioRepository.save(inventario);

        String valoresNuevos =
                "bodega=" + actualizado.getBodega().getId()
                        + ", producto=" + actualizado.getProducto().getId()
                        + ", stock=" + actualizado.getStock();

        return inventarioMapper.entityToDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Usuario usuario = usuarioAutenticadoService.obtenerUsuario();

        usuarioSesionService.establecerUsuario(usuario.getId());

        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventario no encontrado"));

        String valoresAnteriores =
                "id=" + inventario.getId()
                        + ", bodega=" + inventario.getBodega().getId()
                        + ", producto=" + inventario.getProducto().getId()
                        + ", stock=" + inventario.getStock();

        inventarioRepository.delete(inventario);
    }
}
