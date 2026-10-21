package com.example.usuarios_service.application.services;

import com.example.usuarios_service.application.ports.input.CrearUsuarioUseCase;
import com.example.usuarios_service.application.ports.output.PasswordEncoderPort;
import com.example.usuarios_service.application.ports.output.UsuarioRepositoryPort;
import com.example.usuarios_service.domain.exceptions.DomainException;
import com.example.usuarios_service.domain.models.Usuario;

import java.time.LocalDate;
import java.time.Period;

public class CrearUsuarioService implements CrearUsuarioUseCase {

    private static final Long ID_ROL_PROPIETARIO = 2L; // 1=ADMIN, 2=PROPIETARIO, 3=EMPLEADO, 4=CLIENTE
    private static final Long ID_ROL_EMPLEADO = 3L;
    private static final Long ID_ROL_CLIENTE = 4L;

    private static final String REGEX_CORREO = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String REGEX_CELULAR = "^\\+?\\d{1,12}$"; // Máx 13 caracteres incluyendo '+'
    private static final String REGEX_NUMERICO = "^\\d+$";

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public CrearUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort,
                               PasswordEncoderPort passwordEncoderPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public void crearPropietario(Usuario usuario) {
        validarDatosComunes(usuario);
        validarMayorDeEdad(usuario.getFechaNacimiento());
        validarUnicidad(usuario);

        registrarConRol(usuario, ID_ROL_PROPIETARIO);
    }

    @Override
    public void crearEmpleado(Usuario usuario) {
        validarDatosComunes(usuario);
        validarUnicidad(usuario);

        registrarConRol(usuario, ID_ROL_EMPLEADO);
    }

    @Override
    public void crearCliente(Usuario usuario) {
        validarDatosComunes(usuario);
        validarUnicidad(usuario);

        registrarConRol(usuario, ID_ROL_CLIENTE);
    }

    private void registrarConRol(Usuario usuario, Long idRol) {
        usuario.setClave(passwordEncoderPort.encriptar(usuario.getClave()));
        usuario.setIdRol(idRol);
        usuarioRepositoryPort.guardar(usuario);
    }

    private void validarUnicidad(Usuario usuario) {
        if (usuarioRepositoryPort.existePorCorreo(usuario.getCorreo())) {
            throw new DomainException("El correo electrónico ya está registrado.");
        }
        if (usuarioRepositoryPort.existePorDocumento(usuario.getDocumentoDeIdentidad())) {
            throw new DomainException("El documento de identidad ya está registrado.");
        }
    }

    private void validarDatosComunes(Usuario u) {
        if (u.getCorreo() == null || !u.getCorreo().matches(REGEX_CORREO)) {
            throw new DomainException("Estructura de correo electrónico inválida.");
        }
        if (u.getCelular() == null || u.getCelular().length() > 13 || !u.getCelular().matches(REGEX_CELULAR)) {
            throw new DomainException("El celular debe tener máximo 13 caracteres y solo números (con '+' opcional).");
        }
        if (u.getDocumentoDeIdentidad() == null || !u.getDocumentoDeIdentidad().matches(REGEX_NUMERICO)) {
            throw new DomainException("El documento de identidad debe ser únicamente numérico.");
        }
    }

    private void validarMayorDeEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null || Period.between(fechaNacimiento, LocalDate.now()).getYears() < 18) {
            throw new DomainException("El usuario propietario debe ser mayor de edad (+18 años).");
        }
    }
}