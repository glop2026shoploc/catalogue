package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.config.security.AuthenticatedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

public class ControllerTest {
    public final UUID CLIENT_ID = UUID.randomUUID();
    public static final UUID CITY_ID = UUID.randomUUID();
    @BeforeEach
    void authenticateClient() {
        AuthenticatedUser client = new AuthenticatedUser("client@test.fr", UUID.randomUUID(), CITY_ID,
                "CLIENT", CLIENT_ID, null, null, null);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                client, null, List.of(new SimpleGrantedAuthority("ROLE_CLIENT"))));
    }
    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
