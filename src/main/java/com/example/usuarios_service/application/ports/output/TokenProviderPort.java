package com.example.usuarios_service.application.ports.output;

public interface TokenProviderPort {
    String generarToken(Long idUsuario, String correo, String rol);
}