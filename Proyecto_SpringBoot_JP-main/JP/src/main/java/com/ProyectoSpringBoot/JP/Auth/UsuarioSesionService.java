package com.ProyectoSpringBoot.JP.Auth;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioSesionService {

    private final EntityManager entityManager;

    public UsuarioSesionService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void establecerUsuario(Long usuarioId) {

        entityManager.createNativeQuery(
                        "SET @usuario_actual = :usuarioId"
                )
                .setParameter("usuarioId", usuarioId)
                .executeUpdate();
    }

    @Transactional
    public void limpiarUsuario() {

        entityManager.createNativeQuery(
                        "SET @usuario_actual = NULL"
                )
                .executeUpdate();
    }
}