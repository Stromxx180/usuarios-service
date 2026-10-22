package com.example.usuarios_service.application.ports.output;

public interface PasswordEncoderPort {
    String encriptar(String clavePlana);
    boolean validarClave(String clavePlana, String claveEncriptada);
}