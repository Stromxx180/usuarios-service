package com.example.usuarios_service.application.ports.output;

import com.example.usuarios_service.domain.models.Usuario;

public interface UsuarioRepositoryPort {
    Usuario guardar(Usuario usuario);
    boolean existePorCorreo(String correo);
    boolean existePorDocumento(String documento);
}