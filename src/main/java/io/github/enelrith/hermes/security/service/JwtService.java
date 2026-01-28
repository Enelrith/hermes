package io.github.enelrith.hermes.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtService {

    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Value("${spring.security.jwt.expiration}")
    private Long expiration;

    @Value("${spring.security.jwt.refresh-secret}")
    private String refreshSecret;

    @Value("${spring.security.jwt.refresh-expiration}")
    private Long refreshExpiration;

    private SecretKey secretKey;
    private SecretKey refreshSecretKey;

    @PostConstruct
    private void init() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);

        keyBytes = refreshSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // ACCESS TOKENS

    private SecretKey getAccessSigningKey() {
        return this.secretKey;
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("roles", roles);
        return createToken(claims, userDetails.getUsername());
    }

    public Boolean validateAccessToken(String token, UserDetails userDetails) {
        try {
            Claims claims = extractAllClaims(token);

            if (!claims.get("type").equals("access")) return false;

            final String username = claims.getSubject();
            return username.equals(userDetails.getUsername());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // REFRESH TOKENS

    private SecretKey getRefreshSigningKey() { return this.refreshSecretKey; }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, userDetails.getUsername());
    }

    public Boolean validateRefreshToken(String token, UserDetails userDetails) {
        try {
            Claims claims = extractAllClaims(token);

            if (!claims.get("type").equals("refresh")) return false;

            final String username = claims.getSubject();
            return username.equals(userDetails.getUsername());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // TOKEN CREATION

    private String createToken(Map<String, Object> claims, String username) {
        Instant now = Instant.now();
        Instant expiryDate = now.plusMillis(expiration);

        return Jwts.builder()
                .claims(claims)                    // Custom claims
                .subject(username)                  // Username (Email)
                .issuedAt(Date.from(now))                     // Token creation time
                .expiration(Date.from(expiryDate))            // When token expires
                .signWith(getAccessSigningKey())         // Sign with secret key
                .compact();                        // Build the token
    }

    // Extract username from token
    public String getUsernameFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getAccessSigningKey())       // Verify signature
                .build()
                .parseSignedClaims(token)          // Parse the token
                .getPayload();                     // Get the claims
    }
}
