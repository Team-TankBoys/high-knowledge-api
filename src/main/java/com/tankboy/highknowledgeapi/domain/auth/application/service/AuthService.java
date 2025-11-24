    package com.tankboy.highknowledgeapi.domain.auth.application.service;

    import com.tankboy.highknowledgeapi.domain.auth.presentation.dto.request.SignInRequest;
    import com.tankboy.highknowledgeapi.domain.auth.presentation.dto.response.SignInResponse;
    import com.tankboy.highknowledgeapi.global.security.jwt.support.JWTUtil;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContext;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class AuthService {

        private final AuthenticationManager authenticationManager;
        private final JWTUtil jwtUtil;

        public SignInResponse signIn(SignInRequest request) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.username(), request.password())
                );

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
                
                String accessToken = jwtUtil.createAccessToken(request.username(), "ROLE_USER");
                return new SignInResponse(accessToken);

            } catch (UsernameNotFoundException e) {
                throw new IllegalArgumentException("User not found");

            } catch (BadCredentialsException e) {
                throw new IllegalArgumentException("Invalid credentials");

            } catch (Exception e) {
                throw new IllegalArgumentException("로그인 중 오류 발생" + e.getMessage());
            }
        }

    }
