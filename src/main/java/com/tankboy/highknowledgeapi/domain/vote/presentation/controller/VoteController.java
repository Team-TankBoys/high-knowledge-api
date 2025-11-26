package com.tankboy.highknowledgeapi.domain.vote.presentation.controller;

import com.tankboy.highknowledgeapi.domain.vote.application.service.VoteService;
import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.request.VoteRequest;
import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.response.VoteCountResponse;
import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.response.VoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VoteResponse vote(VoteRequest request) {
        return voteService.createVote(request);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public VoteCountResponse getVote(@PathVariable Long postId) {
        // 특정 postId를 가진 vote Entity를 모두 조회
        return voteService.getVote(postId);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public VoteResponse cancelVote(@PathVariable Long postId) {
        return voteService.deleteVote(postId);
    }

}
