package com.tankboy.highknowledgeapi.domain.vote.application.service;

import com.tankboy.highknowledgeapi.domain.user.domain.entity.UserEntity;
import com.tankboy.highknowledgeapi.domain.user.domain.repository.UserRepository;
import com.tankboy.highknowledgeapi.domain.user.exception.UserNotFoundException;
import com.tankboy.highknowledgeapi.domain.vote.domain.entity.VoteEntity;
import com.tankboy.highknowledgeapi.domain.vote.domain.enums.VoteType;
import com.tankboy.highknowledgeapi.domain.vote.domain.repository.VoteRepository;
import com.tankboy.highknowledgeapi.domain.vote.exception.DuplicateVoteException;
import com.tankboy.highknowledgeapi.domain.vote.exception.VoteNotFoundException;
import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.request.VoteRequest;
import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.response.VoteCountResponse;
import com.tankboy.highknowledgeapi.domain.vote.presentation.dto.response.VoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    @Transactional
    public VoteResponse createVote(VoteRequest request) {
        String username = (String)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity userEntity = userRepository.findByName(username)
                .orElseThrow(UserNotFoundException::new);

        Optional<VoteEntity> presentVote = voteRepository.findByPostIdAndUserId(request.postId(), userEntity.getId());

        if (presentVote.isPresent()) {
            if (request.voteType() == presentVote.get().getType()) {
                throw new DuplicateVoteException();
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
        return VoteResponse.of(vote);
    }

    @Transactional
    public VoteResponse deleteVote(Long postId) {
        String username = (String)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity userEntity = userRepository.findByName(username)
                .orElseThrow(UserNotFoundException::new);

        VoteEntity vote = voteRepository.findByPostIdAndUserId(postId, userEntity.getId())
                .orElseThrow(VoteNotFoundException::new);

        voteRepository.delete(vote);
        return VoteResponse.of(vote);
    }

    public VoteCountResponse getVote(Long postId) {
        List<VoteEntity> votes = voteRepository.findAllByPostId(postId);
        int upVoteCount = 0;
        int downVoteCount = 0;

        for (VoteEntity vote : votes) {
            if (vote.getType() == VoteType.UP) {
                upVoteCount++;
            } else {
                downVoteCount++;
            }
        }

        return new VoteCountResponse(upVoteCount, downVoteCount);
    }
}
