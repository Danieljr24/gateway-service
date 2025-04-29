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

        if (cookies == null || cookies.isEmpty()) {
            logger.error("No se encontró la cookie 'ticket' en la solicitud.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        var token = cookies.get(0).getValue();

        if (token == null || token.isEmpty()) {
            logger.error("El token JWT en la cookie 'ticket' está vacío.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (!jwtUtil.validateToken(token)) {
            logger.error("Token JWT no válido: {}", token);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        var username = jwtUtil.getUsername(token);
        System.out.println(username);
        logger.info("Token JWT válido para usuario: {}", username);

        var mutatedRequest = exchange.getRequest().mutate()
                .header("   Token", username)
                .build();

        var mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

        return chain.filter(mutatedExchange);
    }

}
