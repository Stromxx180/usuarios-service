package com.example.usuarios_service.application.ports.input;

public interface LoginUseCase {
    String login(String correo, String clave);
}