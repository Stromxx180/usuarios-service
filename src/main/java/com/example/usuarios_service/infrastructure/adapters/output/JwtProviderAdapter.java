package com.example.usuarios_service.infrastructure.adapters.output;

import com.example.usuarios_service.application.ports.output.TokenProviderPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProviderAdapter implements TokenProviderPort {

    // Clave secreta de al menos 32 caracteres para HS256
    @Value("${jwt.secret:MiClaveSecretaSuperSeguraParaElRetoPlazoleta2026*}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 horas en milisegundos
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generarToken(Long idUsuario, String correo, String rol) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expiration);

        return Jwts.builder()
                .subject(correo)
                .claim("idUsuario", idUsuario)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validarToken(String token) {
        try {
            extraerClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}