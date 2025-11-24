package com.tankboy.highknowledgeapi.domain.vote.domain.repository;

import com.tankboy.highknowledgeapi.domain.vote.domain.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    Optional<VoteEntity> findByPostIdAndUserId(Long postId, Long userId);

}
