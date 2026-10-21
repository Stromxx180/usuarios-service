package com.example.usuarios_service.infrastructure.adapters.output;

import com.example.usuarios_service.application.ports.output.PasswordEncoderPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encriptar(String password) {
        return encoder.encode(password);
    }
}