package com.tankboy.highknowledgeapi.domain.vote.application.service;

import com.tankboy.highknowledgeapi.domain.user.domain.entity.UserEntity;
import com.tankboy.highknowledgeapi.domain.user.domain.repository.UserRepository;
import com.tankboy.highknowledgeapi.domain.vote.domain.entity.VoteEntity;
import com.tankboy.highknowledgeapi.domain.vote.domain.repository.VoteRepository;
import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.request.VoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createVote(VoteRequest request) {
        String username = (String)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity userEntity = userRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        Optional<VoteEntity> presentVote = voteRepository.findByPostIdAndUserId(request.postId(), userEntity.getId());

        if (presentVote.isPresent()) {
            if (request.voteType() == presentVote.get().getType()) {
                throw new IllegalArgumentException("User has already voted on this post");
            } else {
                voteRepository.delete(presentVote.get());
            }
        }

        VoteEntity vote = VoteEntity.builder()
                .postId(request.postId())
                .userId(userEntity.getId())
                .type(request.voteType())
                .build();
        voteRepository.save(vote);
    }

}
