package com.example.usuarios_service.infrastructure.config;

import com.example.usuarios_service.infrastructure.adapters.output.JwtProviderAdapter;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProviderAdapter jwtProviderAdapter;

    public JwtAuthenticationFilter(JwtProviderAdapter jwtProviderAdapter) {
        this.jwtProviderAdapter = jwtProviderAdapter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtProviderAdapter.validarToken(token)) {
                Claims claims = jwtProviderAdapter.extraerClaims(token);
                String correo = claims.getSubject();
                String rol = claims.get("rol", String.class);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        correo,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(rol))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}