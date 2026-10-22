package com.example.usuarios_service.infrastructure.config;

import com.example.usuarios_service.application.ports.input.CrearUsuarioUseCase;
import com.example.usuarios_service.application.ports.input.LoginUseCase;
import com.example.usuarios_service.application.ports.output.PasswordEncoderPort;
import com.example.usuarios_service.application.ports.output.TokenProviderPort;
import com.example.usuarios_service.application.ports.output.UsuarioRepositoryPort;
import com.example.usuarios_service.application.services.CrearUsuarioService;
import com.example.usuarios_service.application.services.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CrearUsuarioUseCase crearUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort,
                                                   PasswordEncoderPort passwordEncoderPort) {
        return new CrearUsuarioService(usuarioRepositoryPort, passwordEncoderPort);
    }

    @Bean
    public LoginUseCase loginUseCase(UsuarioRepositoryPort usuarioRepositoryPort,
                                     PasswordEncoderPort passwordEncoderPort,
                                     TokenProviderPort tokenProviderPort) {
        return new LoginService(usuarioRepositoryPort, passwordEncoderPort, tokenProviderPort);
    }
}