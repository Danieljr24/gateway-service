package com.gateway.gateway_service.config;

import com.gateway.gateway_service.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public GatewayConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Ruta para el microservicio "themis-service", protegida con el filtro JWT
                .route("themis-service", r -> r.path("/themis/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("http://localhost:8082"))

                // Ruta para el microservicio "olimpo-service", protegida con el filtro JWT
                .route("olimpo-service", r -> r.path("/olimpo/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("http://localhost:8081"))

                // Rutas para autenticación y registro (sin filtro JWT)
                .route("olimpo-auth-service", r -> r.path("/olimpo/auth/**")
                        .uri("http://localhost:8081"))  // No pasa por el filtro JWT

                .build();
    }
}
