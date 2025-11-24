package com.tankboy.highknowledgeapi.global.security.jwt.support;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JWTUtil {

    public static final String ACCESS_TOKEN_TYPE = "access";
    private static final SecretKey SECRET_KEY =
            new SecretKeySpec("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    private static final long ACCESS_TOKEN_EXPIRATION = 3600000L;

    public String getJwtFromHttpRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7);
    }

    public String getJti(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .get("jti", String.class);
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .get("role", String.class);
    }

    public String getTokenType(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .get("type", String.class);
    }

    public Boolean isTokenExpired(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public String createAccessToken(String username, String role) {
        return createToken(username, role, ACCESS_TOKEN_TYPE, ACCESS_TOKEN_EXPIRATION);
    }

    public String createToken(String username, String role, String tokenType, Long expiredMs) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claim("type", tokenType)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis())) // 발급 시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 시간
                .signWith(SECRET_KEY) // 비밀키를 사용하여 서명
                .compact();
    }
}