package com.tankboy.highknowledgeapi.domain.vote.presentation.dto.response;

import com.tankboy.highknowledgeapi.domain.vote.domain.entity.VoteEntity;
import com.tankboy.highknowledgeapi.domain.vote.domain.enums.VoteType;

public record VoteResponse(
        Long postId,
        Long userId,
        VoteType voteType
) {

    public static VoteResponse of(VoteEntity entity) {
        return new VoteResponse(
                entity.getPostId(),
                entity.getUserId(),
                entity.getType()
        );
    }

}
