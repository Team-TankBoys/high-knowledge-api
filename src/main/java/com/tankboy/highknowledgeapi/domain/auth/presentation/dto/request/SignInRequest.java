package com.tankboy.highknowledgeapi.domain.auth.presentation.dto.request;

public record SignInRequest(
        String username,
        String password
) {
}
