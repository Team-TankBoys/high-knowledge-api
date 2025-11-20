package com.tankboy.highknowledgeapi.global.security.jwt.filter;

import ch.qos.logback.core.util.StringUtil;
import com.tankboy.highknowledgeapi.global.security.jwt.support.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtUtil.getJwtFromHttpRequest(request);

            if (StringUtil.isNullOrEmpty(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtUtil.getTokenType(token).equals("access")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 토큰");
                return;
            }

            if (jwtUtil.isTokenExpired(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 토큰");
                return;
            }

            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

            if (username == null || role == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 토큰");
                return;
            }

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singleton(new SimpleGrantedAuthority(role))
                    );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

        } catch (Exception e) {
            log.error("JWT 인증 중 오류 발생: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}