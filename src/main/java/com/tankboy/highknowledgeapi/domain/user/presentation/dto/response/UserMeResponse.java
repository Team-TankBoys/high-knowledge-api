package com.tankboy.highknowledgeapi.domain.user.presentation.dto.response;

import com.tankboy.highknowledgeapi.domain.user.domain.entity.UserEntity;

public record UserMeResponse(
        Long id,
        String name,
        String password,
        String email
) {

    public static UserMeResponse of(UserEntity entity) {
        return new UserMeResponse(
                entity.getId(),
                entity.getName(),
                entity.getPassword(),
                entity.getEmail()
        );
    }

}
