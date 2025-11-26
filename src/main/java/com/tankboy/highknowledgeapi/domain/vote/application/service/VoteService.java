package com.tankboy.highknowledgeapi.domain.vote.application.service;

import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.request.VoteRequest;
import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.response.VoteResponse;

public interface VoteService {
    void createVote(VoteRequest request);

    void deleteVote(Long id);

    VoteResponse getVote(Long id);
}
