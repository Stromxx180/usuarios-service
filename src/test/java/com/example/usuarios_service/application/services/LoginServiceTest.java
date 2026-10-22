package com.example.usuarios_service.application.services;

import com.example.usuarios_service.application.ports.output.PasswordEncoderPort;
import com.example.usuarios_service.application.ports.output.TokenProviderPort;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private TokenProviderPort tokenProviderPort;

    @InjectMocks
    private LoginService loginService;

    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        usuarioMock = new Usuario(
                1L,
                "Admin",
                "Sistema",
                "123456789",
                "+573001234567",
                LocalDate.of(1990, 5, 15),
                "admin@plazoleta.com",
                "$2a$10$hashEncriptado",
                1L // 1 = ROLE_ADMINISTRADOR
        );
    }

    @Test
    void login_Exitoso_RetornaTokenJwt() {
        // Arrange
        when(usuarioRepositoryPort.obtenerPorCorreo("admin@plazoleta.com"))
                .thenReturn(Optional.of(usuarioMock));
        when(passwordEncoderPort.validarClave("admin123", "$2a$10$hashEncriptado"))
                .thenReturn(true);
        when(tokenProviderPort.generarToken(1L, "admin@plazoleta.com", "ROLE_ADMINISTRADOR"))
                .thenReturn("token.jwt.simulado");

        // Act
        String token = loginService.login("admin@plazoleta.com", "admin123");

        // Assert
        assertNotNull(token);
        assertEquals("token.jwt.simulado", token);
        verify(tokenProviderPort, times(1)).generarToken(1L, "admin@plazoleta.com", "ROLE_ADMINISTRADOR");
    }

    @Test
    void login_UsuarioNoExiste_LanzaDomainException() {
        // Arrange
        when(usuarioRepositoryPort.obtenerPorCorreo("noexiste@plazoleta.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () ->
                loginService.login("noexiste@plazoleta.com", "clave123")
        );
        assertEquals("Credenciales inválidas", ex.getMessage());
        verify(tokenProviderPort, never()).generarToken(anyLong(), anyString(), anyString());
    }

    @Test
    void login_ClaveIncorrecta_LanzaDomainException() {
        // Arrange
        when(usuarioRepositoryPort.obtenerPorCorreo("admin@plazoleta.com"))
                .thenReturn(Optional.of(usuarioMock));
        when(passwordEncoderPort.validarClave("claveErronea", "$2a$10$hashEncriptado"))
                .thenReturn(false);

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () ->
                loginService.login("admin@plazoleta.com", "claveErronea")
        );
        assertEquals("Credenciales inválidas", ex.getMessage());
        verify(tokenProviderPort, never()).generarToken(anyLong(), anyString(), anyString());
    }
}