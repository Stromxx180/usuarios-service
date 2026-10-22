package com.example.usuarios_service.infrastructure.adapters.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringDataUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByCorreo(String correo);
    boolean existsByDocumentoDeIdentidad(String documentoDeIdentidad);
    Optional<UsuarioEntity> findByCorreo(String correo);
}