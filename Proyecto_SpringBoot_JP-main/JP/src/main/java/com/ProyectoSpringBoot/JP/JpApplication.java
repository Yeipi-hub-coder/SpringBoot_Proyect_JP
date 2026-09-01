package com.ProyectoSpringBoot.JP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpApplication.class, args);

        System.out.println("funcionando");
    }

}