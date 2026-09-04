package com.ProyectoSpringBoot.JP.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productoExamen")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ProductoExamen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "bodega")
    private BodegaExamen bodega;
}
