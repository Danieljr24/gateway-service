package com.gateway.gateway_service.filter;

import com.gateway.gateway_service.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var cookies = request.getCookies().get("ticket");

        // Verificar si la cookie está presente
        if (cookies == null || cookies.isEmpty()) {
            logger.error("No se encontró la cookie 'ticket' en la solicitud.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        var token = cookies.get(0).getValue();

        // Verificar si el token está vacío
        if (token == null || token.isEmpty()) {
            logger.error("El token JWT en la cookie 'ticket' está vacío.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Validar el token
        if (!jwtUtil.validateToken(token)) {
            logger.error("Token JWT no válido: {}", token);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Continuar con el siguiente filtro si el token es válido
        logger.info("Token JWT válido.");
        return chain.filter(exchange);
    }
}
