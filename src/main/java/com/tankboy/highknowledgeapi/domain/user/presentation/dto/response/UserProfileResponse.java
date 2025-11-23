package com.tankboy.highknowledgeapi.domain.user.presentation.dto.response;

import com.tankboy.highknowledgeapi.domain.user.domain.entity.UserEntity;

public record UserProfileResponse(
        Long id,
        String name,
        String email
) {

    public static UserProfileResponse of(UserEntity entity) {
        return new UserProfileResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail()
        );
    }

}
