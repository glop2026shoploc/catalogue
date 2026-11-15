package com.glop.catalogue.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class Authenticator {

    private final JwtParser jwtParser;

    public Authenticator(SecretKey jwtKey) {
        this.jwtParser = Jwts.parser()
                .verifyWith(jwtKey)
                .build();
    }

    /**
     * IA
     * Vérifie la signature et l'expiration du token puis extrait l'utilisateur.
     *
     * @throws JwtException si le token est invalide, expiré ou non signé
     */
    public AuthenticatedUser authenticate(String token) {
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        return AuthenticatedUser.from(claims);
    }
}