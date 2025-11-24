package com.tankboy.highknowledgeapi.domain.auth.presentation.controller;

import com.tankboy.highknowledgeapi.domain.auth.application.service.AuthService;
import com.tankboy.highknowledgeapi.domain.auth.presentation.dto.request.SignInRequest;
import com.tankboy.highknowledgeapi.domain.auth.presentation.dto.response.SignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public SignInResponse signIn(@RequestBody SignInRequest request) {
        return authService.signIn(request);
    }

}
