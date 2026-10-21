package com.example.usuarios_service.infrastructure.adapters.input.rest;

import com.example.usuarios_service.application.ports.input.CrearUsuarioUseCase;
import com.example.usuarios_service.domain.models.Usuario;
import com.example.usuarios_service.infrastructure.adapters.input.rest.dto.CrearPropietarioRequestDto;
import com.example.usuarios_service.infrastructure.adapters.input.rest.dto.CrearUsuarioBasicoRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final CrearUsuarioUseCase crearUsuarioUseCase;

    public UsuarioController(CrearUsuarioUseCase crearUsuarioUseCase) {
        this.crearUsuarioUseCase = crearUsuarioUseCase;
    }

    @Operation(summary = "HU-01: Crear usuario Propietario", description = "Solo ejecutable por un Administrador")
    @ApiResponse(responseCode = "201", description = "Propietario creado exitosamente")
    @PostMapping("/propietario")
    public ResponseEntity<Void> crearPropietario(@Valid @RequestBody CrearPropietarioRequestDto dto) {
        Usuario nuevoUsuario = new Usuario(
                null,
                dto.getNombre(),
                dto.getApellido(),
                dto.getDocumentoDeIdentidad(),
                dto.getCelular(),
                dto.getFechaNacimiento(),
                dto.getCorreo(),
                dto.getClave(),
                null
        );
        crearUsuarioUseCase.crearPropietario(nuevoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "HU-06: Crear usuario Empleado", description = "Solo ejecutable por un Propietario")
    @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente")
    @PostMapping("/empleado")
    public ResponseEntity<Void> crearEmpleado(@Valid @RequestBody CrearUsuarioBasicoRequestDto dto) {
        Usuario nuevoEmpleado = new Usuario(
                null,
                dto.getNombre(),
                dto.getApellido(),
                dto.getDocumentoDeIdentidad(),
                dto.getCelular(),
                null, // Empleado no requiere fecha de nacimiento obligatoria
                dto.getCorreo(),
                dto.getClave(),
                null
        );
        crearUsuarioUseCase.crearEmpleado(nuevoEmpleado);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "HU-08: Crear usuario Cliente", description = "Registro público para clientes")
    @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente")
    @PostMapping("/cliente")
    public ResponseEntity<Void> crearCliente(@Valid @RequestBody CrearUsuarioBasicoRequestDto dto) {
        Usuario nuevoCliente = new Usuario(
                null,
                dto.getNombre(),
                dto.getApellido(),
                dto.getDocumentoDeIdentidad(),
                dto.getCelular(),
                null, // Cliente no requiere fecha de nacimiento obligatoria
                dto.getCorreo(),
                dto.getClave(),
                null
        );
        crearUsuarioUseCase.crearCliente(nuevoCliente);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}