package com.glop.catalogue.config.security;


import io.jsonwebtoken.Claims;

import java.util.UUID;

public record AuthenticatedUser(
        String email,
        UUID userId,
        UUID cityId,
        String role,          // CLIENT, ADMIN, MERCHANT
        UUID clientId,      // si CLIENT
        UUID shopId,        // si MERCHANT
        UUID employeeId,    // si MERCHANT
        String employeeRole   // OWNER ou EMPLOYEE
) {
    static AuthenticatedUser from(Claims c) {
        return new AuthenticatedUser(
                c.getSubject(),
                uuid(c, "userId"),
                uuid(c, "cityId"),
                c.get("role", String.class),
                uuid(c, "clientId"),
                uuid(c, "shopId"),
                uuid(c, "employeeId"),
                c.get("employeeRole", String.class)
        );
    }

    /** Les UUID sont sérialisés en String dans le JWT : jjwt ne sait pas les convertir seul. */
    private static UUID uuid(Claims c, String name) {
        String value = c.get(name, String.class);
        return value == null ? null : UUID.fromString(value);
    }
}