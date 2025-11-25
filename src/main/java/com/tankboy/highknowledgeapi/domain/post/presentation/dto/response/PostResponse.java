package com.tankboy.highknowledgeapi.domain.post.presentation.dto.response;

import com.tankboy.highknowledgeapi.domain.post.domain.entity.PostEntity;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static PostResponse of(PostEntity entity) {
        return new PostResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
