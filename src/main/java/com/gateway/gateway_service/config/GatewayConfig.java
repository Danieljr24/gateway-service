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

    //CAMBIO A GRAPHQL

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("olimpo-auth", r -> r.path("/auth/**")
                        .uri("http://localhost:8081"))

                .route("olimpo-profile", r -> r.path("/auth/user/profile")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("http://localhost:8081"))

                .build();

                //CREAR RUTAS PARA LLAMAR RESTO DE MICROSERVICIOS 
                //DE THEMIS = FICHAS LLENAR FICHA VALIDANDO EXISTENCIA DE USUARIO CON kERBEROS
    }
}
