package com.ProyectoSpringBoot.JP.config;

import com.ProyectoSpringBoot.JP.Model.Enums.RolEnum;
import com.ProyectoSpringBoot.JP.Model.Usuario;
import com.ProyectoSpringBoot.JP.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner inicializarUsuarios(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            if (usuarioRepository.count() == 0) {

                Usuario admin = new Usuario();

                admin.setNombre("Administrador");
                admin.setEmail("admin@logitrack.com");
                admin.setPassword(
                        passwordEncoder.encode("Admin123")
                );
                admin.setRol(RolEnum.ADMIN);

                usuarioRepository.save(admin);
            }
        };
    }
}