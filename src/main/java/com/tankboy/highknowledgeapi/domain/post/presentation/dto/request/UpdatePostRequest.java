package com.tankboy.highknowledgeapi.domain.post.presentation.dto.request;

import java.util.Date;

public record UpdatePostRequest(
        String title,
        Date timeDate,
        String content,
        String password
) {
}
