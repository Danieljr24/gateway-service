package com.gateway.gateway_service.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {
    private final String SECRET_KEY = "123456789012346890123456781234567891234567890123456789";  // Debe coincidir con el de olimpo

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public String getUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", "ROLE_USER");  // Agregar roles si es necesario
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))  // 1 hora
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();
    }
}

