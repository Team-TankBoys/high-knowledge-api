package com.tankboy.highknowledgeapi.domain.vote.presentation.dto.response;

public record VoteResponse(
        int upVoteCount,
        int downVoteCount
) {
    public static VoteResponse of(int upVoteCount, int downVoteCount) {
        return new VoteResponse(upVoteCount, downVoteCount);
    }
}
