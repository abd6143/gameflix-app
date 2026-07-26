package com.gameflix.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.gameflix.config.JwtConfig;
import com.gameflix.entity.Role;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private final JwtConfig jwtConfig;
    private final TokenBlocklistService tokenBlocklistService;

    public JwtService(JwtConfig jwtConfig, TokenBlocklistService tokenBlocklistService) {
        this.jwtConfig = jwtConfig;
        this.tokenBlocklistService = tokenBlocklistService;
    }

    public String generateAccessToken(String username, Role role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());
        return buildToken(claims, username, jwtConfig.getAccessTokenExpirationMs());
    }

    public String generateRefreshToken(String username, Role role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());
        claims.put("type", "refresh");
        return buildToken(claims, username, jwtConfig.getRefreshTokenExpirationMs());
    }

    public boolean isTokenValid(String token, String username) {
        if (tokenBlocklistService.isBlocked(token)) {
            return false;
        }
        String tokenUsername = extractUsername(token);
        return tokenUsername.equals(username) && !isTokenExpired(token);
    }

    public boolean isRefreshToken(String token) {
        try {
            Object type = extractAllClaims(token).get("type");
            return "refresh".equals(type);
        } catch (Exception ex) {
            return false;
        }
    }

    public void invalidateToken(String token) {
        try {
            Date expiration = extractExpiration(token);
            tokenBlocklistService.block(token, expiration.getTime());
            log.info("Token invalidated until {}", expiration);
        } catch (Exception ex) {
            log.warn("Could not invalidate token: {}", ex.getMessage());
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Role extractRole(String token) {
        String role = extractAllClaims(token).get("role", String.class);
        return Role.valueOf(role);
    }

    private String buildToken(Map<String, Object> claims, String subject, long expirationMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtConfig.getJwtSecret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            keyBytes = Decoders.BASE64.decode(
                    java.util.Base64.getEncoder().encodeToString(keyBytes));
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public long getAccessTokenExpirationMs() {
        return jwtConfig.getAccessTokenExpirationMs();
    }
}
