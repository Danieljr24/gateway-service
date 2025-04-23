package com.gateway.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import static org.springframework.security.config.web.server.ServerHttpSecurity.*;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para evitar errores en Postman
                .authorizeExchange(exchange -> exchange
                        .anyExchange().permitAll() // Permite todas las rutas (lo controla tu filtro)
                )
                .httpBasic(Customizer.withDefaults()) // Evita login form por defecto
                .build();
    }
}
