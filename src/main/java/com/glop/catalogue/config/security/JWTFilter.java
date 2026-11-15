package com.glop.catalogue.config.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/** IA
 * Lit le header "Authorization: Bearer <jwt>", valide le token et place
 * l'{@link AuthenticatedUser} dans le SecurityContext.
 * Un token absent ou invalide laisse le contexte vide : la SecurityFilterChain
 * renvoie alors 401 sur les routes protégées.
 */
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final Authenticator authenticator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(BEARER_PREFIX.length()).trim();
        try {
            AuthenticatedUser user = authenticator.authenticate(jwt);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                List<SimpleGrantedAuthority> authorities = user.role() == null
                        ? List.of()
                        : List.of(new SimpleGrantedAuthority("ROLE_" + user.role()));
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException | IllegalArgumentException e) {
            logger.debug("JWT rejeté : " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}