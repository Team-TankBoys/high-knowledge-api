package com.tankboy.highknowledgeapi.domain.vote.presentation.controller;

import com.tankboy.highknowledgeapi.domain.vote.application.service.VoteService;
import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.request.VoteRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void vote(VoteRequest request) {
        voteService.createVote(request);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getVote(@PathVariable Long postId) {
        throw new NotImplementedException();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelVote(@PathVariable Long postId) {
        throw new NotImplementedException();
    }

}
