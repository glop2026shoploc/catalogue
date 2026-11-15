package com.glop.catalogue.config.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true) // active @RolesAllowed
/**
IA
 */
public class SecurityConfig {

    /** Rôle des tokens techniques émis par les autres services (ex. users). */
    public static final String SERVICE_ROLE = "SERVICE";

    private static final String[] PUBLIC_ENDPOINTS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/health/**",
            "/actuator/prometheus",
            "/error"
    };

    @Value("${app.secret-key}")
    private String secretKey;

    @Bean
    public SecretKey jwtKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, Authenticator authenticator) throws Exception {
        // Le filtre n'est volontairement pas un @Component : sinon Spring Boot
        // l'enregistrerait aussi comme filtre servlet global (double exécution).
        JWTFilter jwtFilter = new JWTFilter(authenticator);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        // Formulaire d'inscription commerçant (avant connexion)
                        .requestMatchers(HttpMethod.GET, "/categories").permitAll()
                        // Appel service-à-service depuis users lors de l'inscription commerçant
                        .requestMatchers(HttpMethod.POST, "/shops").hasRole(SERVICE_ROLE)
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}