package com.tankboy.highknowledgeapi.domain.post.presentation.dto.request;

import java.util.Date;

public record CreatePostRequest(
        String title,
        Date timeDate,
        String content,
        String password
) {
}
