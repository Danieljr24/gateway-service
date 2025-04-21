package com.gateway.gateway_service.util;

import io.jsonwebtoken.*;

import org.springframework.stereotype.Component; // Añade esta importación

@Component // Anotación para registrar como bean de Spring
public class JwtUtil {

    private final String SECRET_KEY = "secreto_kerberos"; // Usa la misma clave que en olimpo

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
