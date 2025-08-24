package com.clearskin_ai.userservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // User endpoints
                        .requestMatchers("/api/v1/user/register", "/api/v1/user/login").permitAll()
                        .requestMatchers("/api/v1/user/admin/register").hasRole("ADMIN")

                        // Product endpoints
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").authenticated()// Anyone can view products
                        .requestMatchers(HttpMethod.POST, "/api/v1/products").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasAnyRole("ADMIN", "STAFF")

                        // All other requests
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
