package com.SpringSecurityJWT.security.filter;

import com.SpringSecurityJWT.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            jwtService.authenticateFromRequest(request);
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }
}