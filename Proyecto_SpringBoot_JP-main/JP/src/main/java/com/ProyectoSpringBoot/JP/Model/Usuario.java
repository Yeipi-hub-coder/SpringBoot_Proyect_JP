package com.ProyectoSpringBoot.JP.Model;

import com.ProyectoSpringBoot.JP.Model.Enums.RolEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "usuario")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolEnum rol;

    @OneToMany(mappedBy = "usuario")
    private List<Movimiento> movimientos;

    @OneToMany(mappedBy = "usuario")
    private List<Auditoria> auditorias;
}