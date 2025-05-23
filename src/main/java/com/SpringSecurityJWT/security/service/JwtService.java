package com.SpringSecurityJWT.security.service;

import com.SpringSecurityJWT.common.exception.ApiException;
import com.SpringSecurityJWT.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public String createToken(UserDetails user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        user.getAuthorities().forEach(a -> claims.put(a.getAuthority(), true));
        return jwtUtil.generateToken(claims, user.getUsername());
    }

    public void authenticateFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ApiException("Falta token JWT");
        }
        String token = header.substring(7);
        Claims claims = jwtUtil.parseToken(token);
        String username = claims.getSubject();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}