package com.ProyectoSpringBoot.JP.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "producto")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private BigDecimal precio;

    @OneToMany(mappedBy = "producto")
    private List<Inventario> inventarios;

    @OneToMany(mappedBy = "producto")
    private List<MovimientoDetalle> movimientosDetalles;
}