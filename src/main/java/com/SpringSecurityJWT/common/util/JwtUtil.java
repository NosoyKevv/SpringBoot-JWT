package com.SpringSecurityJWT.common.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private final String secret = "SECRET_KEY";
    private final long expiration = 1000 * 60 * 60; // 1h

    public String generateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Claims parseToken(String token) throws JwtException {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}