package com.glop.catalogue.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
public class JWTFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if(authHeader == null  && !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
        }
        /*try{

        }c
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            CustomUserDetails customUserDetails = authPortIn.findUserModelByUserEmail(username)
                    .map(jwtAuthMapper::toCustomUserDetails)
                    .orElse(null);
            if(customUserDetails != null && jwtUtils.validateToken(jwt,customUserDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails,null,
                        customUserDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);*/
    }
}
