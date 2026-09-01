package com.ProyectoSpringBoot.JP.Repository;

import com.ProyectoSpringBoot.JP.Model.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodegaRepository extends JpaRepository<Bodega, Long> {
}
