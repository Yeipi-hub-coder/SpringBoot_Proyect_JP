package com.ProyectoSpringBoot.JP.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "bodega")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Bodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
    private String encargado;

    @OneToMany(mappedBy = "bodega")
    private List<Inventario> inventarios;

    @OneToMany(mappedBy = "bodegaOrigen")
    private List<Movimiento> movimientosOrigen;

    @OneToMany(mappedBy = "bodegaDestino")
    private List<Movimiento> movimientosDestino;
}