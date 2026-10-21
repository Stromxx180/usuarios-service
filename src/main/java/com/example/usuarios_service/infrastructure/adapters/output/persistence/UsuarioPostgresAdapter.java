package com.example.usuarios_service.infrastructure.adapters.output.persistence;

import com.example.usuarios_service.application.ports.output.UsuarioRepositoryPort;
import com.example.usuarios_service.domain.models.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioPostgresAdapter implements UsuarioRepositoryPort {

    private final SpringDataUsuarioRepository springDataUsuarioRepository;

    public UsuarioPostgresAdapter(SpringDataUsuarioRepository springDataUsuarioRepository) {
        this.springDataUsuarioRepository = springDataUsuarioRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        // 1. Convertir del modelo de Dominio a la Entidad JPA
        UsuarioEntity entity = toEntity(usuario);

        // 2. Guardar en PostgreSQL usando Spring Data
        UsuarioEntity savedEntity = springDataUsuarioRepository.save(entity);

        // 3. Convertir de vuelta a Modelo de Dominio y retornar
        return toDomain(savedEntity);
    }

    @Override
    public boolean existePorCorreo(String correo) {
        return springDataUsuarioRepository.existsByCorreo(correo);
    }

    @Override
    public boolean existePorDocumento(String documento) {
        return springDataUsuarioRepository.existsByDocumentoDeIdentidad(documento);
    }

    // --- MÉTODOS DE MAPEOMIN (Traducir de Dominio <-> Entidad JPA) ---

    private UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getDocumentoDeIdentidad(),
                usuario.getCelular(),
                usuario.getFechaNacimiento(),
                usuario.getCorreo(),
                usuario.getClave(),
                usuario.getIdRol()
        );
    }

    private Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Usuario(
                entity.getId(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getDocumentoDeIdentidad(),
                entity.getCelular(),
                entity.getFechaNacimiento(),
                entity.getCorreo(),
                entity.getClave(),
                entity.getIdRol()
        );
    }
}