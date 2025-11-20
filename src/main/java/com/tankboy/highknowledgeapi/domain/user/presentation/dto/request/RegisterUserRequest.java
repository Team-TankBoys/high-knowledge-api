package com.tankboy.highknowledgeapi.domain.user.presentation.dto.request;

public record RegisterUserRequest(
        String name,
        String password,
        String email
) {
}
