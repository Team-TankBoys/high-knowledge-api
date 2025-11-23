package com.tankboy.highknowledgeapi.domain.user.presentation.dto.request;

public record UpdateUserRequest(
        String name,
        String password,
        String email
) {
}
