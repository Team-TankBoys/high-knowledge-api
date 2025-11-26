package com.tankboy.highknowledgeapi.domain.vote.presentation.dto.response;

public record VoteCountResponse(
        int upVoteCount,
        int downVoteCount
) {
    public static VoteCountResponse of(int upVoteCount, int downVoteCount) {
        return new VoteCountResponse(upVoteCount, downVoteCount);
    }
}
