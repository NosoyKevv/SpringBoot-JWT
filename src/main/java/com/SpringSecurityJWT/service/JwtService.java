package com.SpringSecurityJWT.service;

import com.SpringSecurityJWT.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-minutes}")
    private long EXPIRATION_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(User user, Map<String, Object> extraClaims) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + (EXPIRATION_MINUTES * 60 * 1000));

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername()) // <<-- Aquí usa username, no name
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String jwt, User user) {
        final String username = extractUsername(jwt);
        return username.equals(user.getUsername()) && !isTokenExpired(jwt);
    }

    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    public List<GrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        List<Map<String, String>> permissions = (List<Map<String, String>>) claims.get("permission");

        if (permissions == null) return Collections.emptyList();

        // Aquí asegúrate de que lo que devuelves sea una lista de GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Map<String, String> permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.get("authority")));
        }

        return authorities;
    }

    private boolean isTokenExpired(String jwt) {
        Date expiration = extractAllClaims(jwt).getExpiration();
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key generateKey() {
        byte[] secretAsBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(secretAsBytes);
    }
}
