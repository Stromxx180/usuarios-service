package com.example.usuarios_service.infrastructure.config;

import com.example.usuarios_service.application.ports.output.PasswordEncoderPort;
import com.example.usuarios_service.application.ports.output.UsuarioRepositoryPort;
import com.example.usuarios_service.domain.models.Usuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(UsuarioRepositoryPort usuarioRepositoryPort,
                                           PasswordEncoderPort passwordEncoderPort) {
        return args -> {
            String adminEmail = "admin@plazoleta.com";
            if (!usuarioRepositoryPort.existePorCorreo(adminEmail)) {
                Usuario admin = new Usuario(
                        null,
                        "Admin",
                        "Principal",
                        "1000000000",
                        "+573000000000",
                        LocalDate.of(1995, 1, 1),
                        adminEmail,
                        passwordEncoderPort.encriptar("admin123"),
                        1L // 1 = ROLE_ADMINISTRADOR
                );
                usuarioRepositoryPort.guardar(admin);
                System.out.println(">>> Usuario ADMINISTRADOR creado por defecto: " + adminEmail + " / clave: admin123");
            }
        };
    }
}