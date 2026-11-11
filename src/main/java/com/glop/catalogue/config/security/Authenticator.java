package com.glop.catalogue.config.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
@Component
@RequiredArgsConstructor
public class Authenticator {
    private final SecretKey secretKeySpec;
    void authenticate(String token) throws Exception {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKeySpec)
                .build();
        try {
            jwtParser.parse(token);
        } catch (Exception e) {
            throw new Exception("Could not verify JWT token integrity!", e);
        }
    }

}
