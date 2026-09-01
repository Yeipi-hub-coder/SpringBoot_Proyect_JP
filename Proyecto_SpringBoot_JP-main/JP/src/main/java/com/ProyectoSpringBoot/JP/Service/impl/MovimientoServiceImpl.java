package com.ProyectoSpringBoot.JP.Service.impl;

import com.ProyectoSpringBoot.JP.Auth.UsuarioAutenticadoService;
import com.ProyectoSpringBoot.JP.Auth.UsuarioSesionService;
import com.ProyectoSpringBoot.JP.Model.Bodega;
import com.ProyectoSpringBoot.JP.Model.Enums.TipoMovimientoEnum;
import com.ProyectoSpringBoot.JP.Model.Inventario;
import com.ProyectoSpringBoot.JP.Model.Movimiento;
import com.ProyectoSpringBoot.JP.Model.MovimientoDetalle;
import com.ProyectoSpringBoot.JP.Model.Producto;
import com.ProyectoSpringBoot.JP.Model.Usuario;
import com.ProyectoSpringBoot.JP.dto.request.MovimientoDetalleRequest;
import com.ProyectoSpringBoot.JP.dto.request.MovimientoRequest;
import com.ProyectoSpringBoot.JP.dto.response.MovimientoResponse;
import com.ProyectoSpringBoot.JP.exception.BadRequestException;
import com.ProyectoSpringBoot.JP.exception.ResourceNotFoundException;
import com.ProyectoSpringBoot.JP.Mapper.MovimientoMapper;
import com.ProyectoSpringBoot.JP.Repository.BodegaRepository;
import com.ProyectoSpringBoot.JP.Repository.InventarioRepository;
import com.ProyectoSpringBoot.JP.Repository.MovimientoRepository;
import com.ProyectoSpringBoot.JP.Repository.ProductoRepository;
import com.ProyectoSpringBoot.JP.Service.MovimientoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final UsuarioSesionService usuarioSesionService;
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;

    public MovimientoServiceImpl(
            MovimientoRepository movimientoRepository,
            MovimientoMapper movimientoMapper,
            BodegaRepository bodegaRepository,
            ProductoRepository productoRepository,
            InventarioRepository inventarioRepository,
            UsuarioSesionService usuarioSesionService,
            UsuarioAutenticadoService usuarioAutenticadoService
    ) {
        this.movimientoRepository = movimientoRepository;
        this.movimientoMapper = movimientoMapper;
        this.bodegaRepository = bodegaRepository;
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
        this.usuarioSesionService = usuarioSesionService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Override
    @Transactional
    public MovimientoResponse crear(MovimientoRequest dto) {

        validarMovimiento(dto);

        Usuario usuario = usuarioAutenticadoService.obtenerUsuario();

        usuarioSesionService.establecerUsuario(usuario.getId());

        Movimiento movimiento = movimientoMapper.dtoToEntity(dto);

        movimiento.setFecha(LocalDateTime.now());
        movimiento.setUsuario(usuario);

        Bodega bodegaOrigen = obtenerBodega(dto.bodegaOrigenId());
        Bodega bodegaDestino = obtenerBodega(dto.bodegaDestinoId());

        movimiento.setBodegaOrigen(bodegaOrigen);
        movimiento.setBodegaDestino(bodegaDestino);

        if (bodegaOrigen != null
                && bodegaDestino != null
                && bodegaOrigen.getId().equals(bodegaDestino.getId())) {

            throw new BadRequestException(
                    "La bodega de origen y destino no pueden ser la misma"
            );
        }

        List<MovimientoDetalle> detalles = new ArrayList<>();

        for (MovimientoDetalleRequest detalleDto : dto.detalles()) {

            Producto producto = productoRepository.findById(detalleDto.productoId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Producto con id "
                                            + detalleDto.productoId()
                                            + " no encontrado"
                            ));

            MovimientoDetalle detalle = new MovimientoDetalle();

            detalle.setMovimiento(movimiento);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDto.cantidad());

            detalles.add(detalle);
        }

        movimiento.setDetalles(detalles);

        actualizarStock(
                dto.tipo(),
                bodegaOrigen,
                bodegaDestino,
                dto.detalles()
        );

        Movimiento guardado = movimientoRepository.save(movimiento);

        return movimientoMapper.entityToDto(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoResponse obtenerPorId(Long id) {

        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Movimiento no encontrado"
                        ));

        return movimientoMapper.entityToDto(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponse> obtenerTodos() {

        return movimientoRepository.findAll()
                .stream()
                .map(movimientoMapper::entityToDto)
                .toList();
    }

    private void validarMovimiento(MovimientoRequest dto) {

        if (dto == null) {
            throw new BadRequestException(
                    "El movimiento es obligatorio"
            );
        }

        if (dto.tipo() == null) {
            throw new BadRequestException(
                    "El tipo de movimiento es obligatorio"
            );
        }

        if (dto.detalles() == null || dto.detalles().isEmpty()) {
            throw new BadRequestException(
                    "Debe existir al menos un producto"
            );
        }

        switch (dto.tipo()) {

            case ENTRADA -> {

                if (dto.bodegaOrigenId() != null) {
                    throw new BadRequestException(
                            "Una entrada no debe tener bodega de origen"
                    );
                }

                if (dto.bodegaDestinoId() == null) {
                    throw new BadRequestException(
                            "Una entrada requiere una bodega de destino"
                    );
                }
            }

            case SALIDA -> {

                if (dto.bodegaOrigenId() == null) {
                    throw new BadRequestException(
                            "Una salida requiere una bodega de origen"
                    );
                }

                if (dto.bodegaDestinoId() != null) {
                    throw new BadRequestException(
                            "Una salida no debe tener bodega de destino"
                    );
                }
            }

            case TRANSFERENCIA -> {

                if (dto.bodegaOrigenId() == null
                        || dto.bodegaDestinoId() == null) {

                    throw new BadRequestException(
                            "Una transferencia requiere bodega de origen y destino"
                    );
                }

                if (dto.bodegaOrigenId().equals(dto.bodegaDestinoId())) {

                    throw new BadRequestException(
                            "La bodega de origen y destino no pueden ser la misma"
                    );
                }
            }
        }
    }

    private Bodega obtenerBodega(Long id) {

        if (id == null) {
            return null;
        }

        return bodegaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bodega con id " + id + " no encontrada"
                        ));
    }

    private Usuario obtenerUsuarioAutenticado() {

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

    private void actualizarStock(
            TipoMovimientoEnum tipo,
            Bodega bodegaOrigen,
            Bodega bodegaDestino,
            List<MovimientoDetalleRequest> detalles
    ) {

        for (MovimientoDetalleRequest detalle : detalles) {

            Producto producto = productoRepository.findById(
                    detalle.productoId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Producto con id "
                                    + detalle.productoId()
                                    + " no encontrado"
                    ));

            Integer cantidad = detalle.cantidad();

            switch (tipo) {

                case ENTRADA -> {

                    Inventario inventarioDestino =
                            obtenerOCrearInventario(
                                    bodegaDestino,
                                    producto
                            );

                    inventarioDestino.setStock(
                            inventarioDestino.getStock() + cantidad
                    );
                }

                case SALIDA -> {

                    Inventario inventarioOrigen =
                            obtenerInventario(
                                    bodegaOrigen,
                                    producto
                            );

                    validarStock(
                            inventarioOrigen,
                            cantidad
                    );

                    inventarioOrigen.setStock(
                            inventarioOrigen.getStock() - cantidad
                    );
                }

                case TRANSFERENCIA -> {

                    Inventario inventarioOrigen =
                            obtenerInventario(
                                    bodegaOrigen,
                                    producto
                            );

                    validarStock(
                            inventarioOrigen,
                            cantidad
                    );

                    Inventario inventarioDestino =
                            obtenerOCrearInventario(
                                    bodegaDestino,
                                    producto
                            );

                    inventarioOrigen.setStock(
                            inventarioOrigen.getStock() - cantidad
                    );

                    inventarioDestino.setStock(
                            inventarioDestino.getStock() + cantidad
                    );
                }
            }
        }
    }

    private Inventario obtenerInventario(
            Bodega bodega,
            Producto producto
    ) {

        return inventarioRepository
                .findByBodegaIdAndProductoId(
                        bodega.getId(),
                        producto.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe inventario para el producto "
                                        + producto.getNombre()
                                        + " en la bodega "
                                        + bodega.getNombre()
                        ));
    }

    private Inventario obtenerOCrearInventario(
            Bodega bodega,
            Producto producto
    ) {

        return inventarioRepository
                .findByBodegaIdAndProductoId(
                        bodega.getId(),
                        producto.getId()
                )
                .orElseGet(() -> {

                    Inventario inventario = new Inventario();

                    inventario.setBodega(bodega);
                    inventario.setProducto(producto);
                    inventario.setStock(0);

                    return inventarioRepository.save(inventario);
                });
    }

    private void validarStock(
            Inventario inventario,
            Integer cantidad
    ) {

        if (inventario.getStock() < cantidad) {

            throw new BadRequestException(
                    "Stock insuficiente para el producto "
                            + inventario.getProducto().getNombre()
                            + ". Stock disponible: "
                            + inventario.getStock()
            );
        }
    }
}