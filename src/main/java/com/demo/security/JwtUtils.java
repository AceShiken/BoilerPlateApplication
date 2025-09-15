package com.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {

    private final SecretKey key;
    private final int expiryMinutes;

    public JwtUtils(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiry-minutes}") int expiryMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiryMinutes = expiryMinutes;
    }

    public String createToken(Long userId, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expiryMinutes * 60L);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .addClaims(Map.of("role", role))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String jwt) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
    }

    public Instant getExpiry(String jwt) {
        Date exp = parse(jwt).getBody().getExpiration();
        return exp.toInstant();
    }

    public Long getUserId(String jwt) {
        return Long.valueOf(parse(jwt).getBody().getSubject());
    }

    public String getRole(String jwt) {
        Object r = parse(jwt).getBody().get("role");
        return r == null ? "USER" : r.toString();
    }
}
