package com.example.usuarios_service.infrastructure.adapters.input.rest;

import com.example.usuarios_service.application.ports.input.LoginUseCase;
import com.example.usuarios_service.infrastructure.adapters.input.rest.dto.JwtResponseDto;
import com.example.usuarios_service.infrastructure.adapters.input.rest.dto.LoginRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        String token = loginUseCase.login(request.getCorreo(), request.getClave());
        return ResponseEntity.ok(new JwtResponseDto(token));
    }
}