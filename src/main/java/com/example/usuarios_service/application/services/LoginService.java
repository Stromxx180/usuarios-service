package com.example.usuarios_service.application.services;

import com.example.usuarios_service.application.ports.input.LoginUseCase;
import com.example.usuarios_service.application.ports.output.PasswordEncoderPort;
import com.example.usuarios_service.application.ports.output.TokenProviderPort;
import com.example.usuarios_service.application.ports.output.UsuarioRepositoryPort;
import com.example.usuarios_service.domain.exceptions.DomainException;
import com.example.usuarios_service.domain.models.Usuario;

public class LoginService implements LoginUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;

    public LoginService(UsuarioRepositoryPort usuarioRepositoryPort,
                        PasswordEncoderPort passwordEncoderPort,
                        TokenProviderPort tokenProviderPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProviderPort = tokenProviderPort;
    }

    @Override
    public String login(String correo, String clave) {
        if (correo == null || correo.isBlank() || clave == null || clave.isBlank()) {
            throw new DomainException("El correo y la clave son obligatorios");
        }

        Usuario usuario = usuarioRepositoryPort.obtenerPorCorreo(correo)
                .orElseThrow(() -> new DomainException("Credenciales inválidas"));

        if (!passwordEncoderPort.validarClave(clave, usuario.getClave())) {
            throw new DomainException("Credenciales inválidas");
        }

        String rolNombre = obtenerNombreRol(usuario.getIdRol());
        return tokenProviderPort.generarToken(usuario.getId(), usuario.getCorreo(), rolNombre);
    }

    private String obtenerNombreRol(Long idRol) {
        if (idRol == null) {
            throw new DomainException("El usuario no tiene un rol asignado");
        }
        return switch (idRol.intValue()) {
            case 1 -> "ROLE_ADMINISTRADOR";
            case 2 -> "ROLE_PROPIETARIO";
            case 3 -> "ROLE_EMPLEADO";
            case 4 -> "ROLE_CLIENTE";
            default -> throw new DomainException("Rol desconocido: " + idRol);
        };
    }
}