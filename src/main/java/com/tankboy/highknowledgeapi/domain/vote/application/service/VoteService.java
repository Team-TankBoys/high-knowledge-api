package com.tankboy.highknowledgeapi.domain.vote.application.service;

import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.request.VoteRequest;
import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.response.VoteCountResponse;
import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.response.VoteResponse;

public interface VoteService {
    VoteResponse createVote(VoteRequest request);

    VoteResponse deleteVote(Long id);

    VoteCountResponse getVote(Long id);
}
