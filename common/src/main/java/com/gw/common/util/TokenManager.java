package com.gw.common.util;

import com.gw.common.domain.UserIdentity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class TokenManager {
    private static final Logger LOG = LogManager.getLogger("APP");
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

    private static Map<String, Object> addClaimsV2(String authorities, String userId) {
        return Map.of("Authorities", authorities,
                "userId", userId);
    }

    public String generateToken(UserIdentity user,
                                Duration tokenDuration) {
        Map<String, Object> claims = addClaimsV2(user.role(), user.id());
        return doGenerateToken(claims, user.userName(), UUID.randomUUID(), tokenDuration);
    }

    public static class Token {
        private final String tokenAsString;
        private final Key signingKey;

        public Token(String tokenAsString, Key signingKey) {
            this.tokenAsString = tokenAsString;
            this.signingKey = signingKey;
        }

        public <T> T getClaimFromToken(Function<Claims, T> claimsResolver) {
            return claimsResolver.apply(parseClaims());
        }

        private Claims parseClaims() {
            return Jwts.parserBuilder().setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(tokenAsString).getBody();
        }

        public String getUserId() {
            try {
                return (String) parseClaims().get("userId");
            } catch (ExpiredJwtException e) {
                LOG.warn("Expired user token found");
                return null;
            }
        }

        private String getUsernameFromToken() {
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
            } catch (Exception e) {
                return false;
            }
        }
    }
}