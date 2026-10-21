package com.example.usuarios_service.infrastructure.adapters.input.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CrearPropietarioRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Pattern(regexp = "^\\d+$", message = "El documento de identidad debe ser únicamente numérico")
    private String documentoDeIdentidad;

    @NotBlank(message = "El celular es obligatorio")
    @Size(max = 13, message = "El celular debe contener un máximo de 13 caracteres")
    @Pattern(regexp = "^\\+?\\d+$", message = "El celular puede contener el símbolo + y números")
    private String celular;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser una estructura de email válida")
    private String correo;

    @NotBlank(message = "La clave es obligatoria")
    private String clave;
}