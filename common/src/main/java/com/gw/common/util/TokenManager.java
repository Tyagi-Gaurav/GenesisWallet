package com.gw.common.util;

import com.gw.common.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class TokenManager {
    private static final Logger LOG = LoggerFactory.getLogger(TokenManager.class);
    private final Key signingKey;

    public TokenManager(Key signingKey) {
        this.signingKey = signingKey;
    }

    public Token parse(String token) {
        return new Token(token, signingKey);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, UUID id,
                                   Duration tokenDuration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(id.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenDuration.toMillis()))
                .signWith(signingKey)
                .compact();
    }

    private static Map<String, Object> addClaimsV2(String authorities) {
        return Map.of("Authorities", authorities);
    }

    public String generateToken(User user,
                                Duration tokenDuration) {
        Map<String, Object> claims = addClaimsV2(user.role());
        return doGenerateToken(claims, user.username(), user.id(), tokenDuration);
    }

    public static class Token {
        private final String tokenAsString;
        private Key signingKey;

        public Token(String tokenAsString, Key signingKey) {
            this.tokenAsString = tokenAsString;
            this.signingKey = signingKey;
        }

        public <T> T getClaimFromToken(Function<Claims, T> claimsResolver) {
            Claims claims = Jwts.parserBuilder().setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(tokenAsString).getBody();
            return claimsResolver.apply(claims);
        }

        public String getUserId() {
            try {
                return getClaimFromToken(Claims::getId);
            } catch(ExpiredJwtException e) {
                LOG.warn("Expired user token found");
                return null;
            }
        }

        public boolean validateDataWith(User user) {
            final String username = getUsernameFromToken();
            return (username != null && username.equals(user.username()));
        }

        public String getUsernameFromToken() {
            return getClaimFromToken(Claims::getSubject);
        }

        public String getRole() {
            return (String) getClaimFromToken(claims -> claims.get("Authorities"));
        }

        @Override
        public String toString() {
            return tokenAsString;
        }

        public boolean isTokenValid() {
            try {
                getUsernameFromToken();
                return true;
            } catch(Exception e) {
                return false;
            }
        }
    }
}