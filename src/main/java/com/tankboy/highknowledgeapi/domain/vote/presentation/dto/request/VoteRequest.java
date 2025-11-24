package com.tankboy.highknowledgeapi.domain.vote.presentation.dto.request;

import com.tankboy.highknowledgeapi.domain.vote.domain.enums.VoteType;

public record VoteRequest(
        Long postId,
        VoteType voteType
) {
}
