package com.example.usuarios_service.application.services;

import com.example.usuarios_service.application.ports.output.PasswordEncoderPort;
import com.example.usuarios_service.application.ports.output.UsuarioRepositoryPort;
import com.example.usuarios_service.domain.exceptions.DomainException;
import com.example.usuarios_service.domain.models.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearUsuarioServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private CrearUsuarioService crearUsuarioService;

    private Usuario propietarioValido;
    private Usuario usuarioBasicoValido;

    @BeforeEach
    void setUp() {
        propietarioValido = new Usuario(
                null, "Carlos", "Pérez", "1045678901",
                "+573005698325", LocalDate.now().minusYears(25),
                "carlos@plazoleta.com", "ClaveSegura123", null
        );

        usuarioBasicoValido = new Usuario(
                null, "Ana", "Gómez", "1098765432",
                "+573012345678", null,
                "ana@plazoleta.com", "ClaveEmpleado123", null
        );
    }

    // --- TESTS HU-01: PROPIETARIO ---

    @Test
    void crearPropietario_Exitoso_CuandoDatosSonValidos() {
        when(usuarioRepositoryPort.existePorCorreo(anyString())).thenReturn(false);
        when(usuarioRepositoryPort.existePorDocumento(anyString())).thenReturn(false);
        when(passwordEncoderPort.encriptar("ClaveSegura123")).thenReturn("$2a$10$hashEncriptado");

        crearUsuarioService.crearPropietario(propietarioValido);

        assertEquals(2L, propietarioValido.getIdRol()); // Verifica rol PROPIETARIO (2L)
        assertEquals("$2a$10$hashEncriptado", propietarioValido.getClave());
        verify(usuarioRepositoryPort, times(1)).guardar(propietarioValido);
    }

    @Test
    void crearPropietario_LanzaExcepcion_CuandoEsMenorDeEdad() {
        propietarioValido.setFechaNacimiento(LocalDate.now().minusYears(17));

        DomainException ex = assertThrows(DomainException.class,
                () -> crearUsuarioService.crearPropietario(propietarioValido));

        assertTrue(ex.getMessage().contains("mayor de edad"));
        verify(usuarioRepositoryPort, never()).guardar(any());
    }

    @Test
    void crearPropietario_LanzaExcepcion_CuandoDocumentoNoEsNumerico() {
        propietarioValido.setDocumentoDeIdentidad("1045ABC");

        assertThrows(DomainException.class,
                () -> crearUsuarioService.crearPropietario(propietarioValido));
        verify(usuarioRepositoryPort, never()).guardar(any());
    }

    // --- TESTS HU-06: EMPLEADO ---

    @Test
    void crearEmpleado_Exitoso_AsignaRolEmpleadoYEncriptaClave() {
        when(usuarioRepositoryPort.existePorCorreo(anyString())).thenReturn(false);
        when(usuarioRepositoryPort.existePorDocumento(anyString())).thenReturn(false);
        when(passwordEncoderPort.encriptar("ClaveEmpleado123")).thenReturn("$2a$10$hashEmpleado");

        crearUsuarioService.crearEmpleado(usuarioBasicoValido);

        assertEquals(3L, usuarioBasicoValido.getIdRol()); // Verifica rol EMPLEADO (3L)
        assertEquals("$2a$10$hashEmpleado", usuarioBasicoValido.getClave());
        verify(usuarioRepositoryPort, times(1)).guardar(usuarioBasicoValido);
    }

    @Test
    void crearEmpleado_LanzaExcepcion_CuandoCorreoYaExiste() {
        when(usuarioRepositoryPort.existePorCorreo("ana@plazoleta.com")).thenReturn(true);

        DomainException ex = assertThrows(DomainException.class,
                () -> crearUsuarioService.crearEmpleado(usuarioBasicoValido));

        assertTrue(ex.getMessage().contains("correo electrónico ya está registrado"));
        verify(usuarioRepositoryPort, never()).guardar(any());
    }

    // --- TESTS HU-08: CLIENTE ---

    @Test
    void crearCliente_Exitoso_AsignaRolClienteYEncriptaClave() {
        when(usuarioRepositoryPort.existePorCorreo(anyString())).thenReturn(false);
        when(usuarioRepositoryPort.existePorDocumento(anyString())).thenReturn(false);
        when(passwordEncoderPort.encriptar("ClaveEmpleado123")).thenReturn("$2a$10$hashCliente");

        crearUsuarioService.crearCliente(usuarioBasicoValido);

        assertEquals(4L, usuarioBasicoValido.getIdRol()); // Verifica rol CLIENTE (4L)
        assertEquals("$2a$10$hashCliente", usuarioBasicoValido.getClave());
        verify(usuarioRepositoryPort, times(1)).guardar(usuarioBasicoValido);
    }
}