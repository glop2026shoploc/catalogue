package com.glop.catalogue.config.security;


import io.jsonwebtoken.Claims;

public record AuthenticatedUser(
        String email,
        String userId,
        String cityId,
        String role,          // CLIENT, ADMIN, MERCHANT
        String clientId,      // si CLIENT
        String shopId,        // si MERCHANT
        String employeeId,    // si MERCHANT
        String employeeRole   // OWNER ou EMPLOYEE
) {
    static AuthenticatedUser from(Claims c) {
        return new AuthenticatedUser(
                c.getSubject(),
                c.get("userId", String.class),
                c.get("cityId", String.class),
                c.get("role", String.class),
                c.get("clientId", String.class),
                c.get("shopId", String.class),
                c.get("employeeId", String.class),
                c.get("employeeRole", String.class)
        );
    }
}