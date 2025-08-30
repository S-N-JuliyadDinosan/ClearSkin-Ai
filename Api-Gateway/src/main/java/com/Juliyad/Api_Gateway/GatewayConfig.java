package com.Juliyad.Api_Gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // Admin Service
                .route("admin-service", r -> r.path("/api/v1/admin/**")
                        .uri("http://localhost:8081"))

                // User Service
                .route("user-service", r -> r.path("/api/v1/user/**")
                        .uri("http://localhost:8082"))

                // Doctor Service
                .route("doctor-service", r -> r.path("/api/v1/doctors/**")
                        .uri("http://localhost:8081"))

                // Product Service
                .route("product-service", r -> r.path("/api/v1/products/**")
                        .uri("http://localhost:8082"))

                //Appointment Service
                .route("appointment-service", r -> r.path("/api/v1/appointments/**")
                        .uri("http://localhost:8081"))

                .build();
    }
}
