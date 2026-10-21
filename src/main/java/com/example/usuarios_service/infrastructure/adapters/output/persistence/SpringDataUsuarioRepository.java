package com.example.usuarios_service.infrastructure.adapters.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByCorreo(String correo);
    boolean existsByDocumentoDeIdentidad(String documentoDeIdentidad);
}