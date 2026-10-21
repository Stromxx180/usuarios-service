package com.example.usuarios_service.infrastructure.adapters.input.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CrearUsuarioBasicoRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Pattern(regexp = "^\\d+$", message = "El documento debe ser únicamente numérico")
    private String documentoDeIdentidad;

    @NotBlank(message = "El celular es obligatorio")
    @Size(max = 13, message = "El celular debe tener máximo 13 caracteres")
    @Pattern(regexp = "^\\+?\\d{1,12}$", message = "Formato de celular inválido")
    private String celular;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Estructura de correo inválida")
    private String correo;

    @NotBlank(message = "La clave es obligatoria")
    private String clave;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDocumentoDeIdentidad() { return documentoDeIdentidad; }
    public void setDocumentoDeIdentidad(String documentoDeIdentidad) { this.documentoDeIdentidad = documentoDeIdentidad; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
}