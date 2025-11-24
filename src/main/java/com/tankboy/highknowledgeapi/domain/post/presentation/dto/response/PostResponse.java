package com.tankboy.highknowledgeapi.domain.post.presentation.dto.response;

import java.util.Date;

public record PostResponse(
        Long id,
        String title,
        Date timeDate,
        String content
) {
}
