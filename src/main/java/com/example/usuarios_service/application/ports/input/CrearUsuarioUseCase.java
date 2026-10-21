package com.example.usuarios_service.application.ports.input;

import com.example.usuarios_service.domain.models.Usuario;

public interface CrearUsuarioUseCase {
    void crearPropietario(Usuario usuario);
    void crearEmpleado(Usuario usuario);
    void crearCliente(Usuario usuario);
}