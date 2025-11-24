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

        // 특정 postId를 가진 vote Entity를 모두 조회
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelVote(@PathVariable Long postId) {
        throw new NotImplementedException();

        // Service 단계에서 현재 인증 중인 유저를 받아온 뒤 (VoteService의 createVote 메서드 참고)
        // 해당 유저가 해당 postId에 대해 투표한 내역이 있는지 확인
        // 있으면 vote 엔티티를 삭제
    }

}
