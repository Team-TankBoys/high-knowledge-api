package com.tankboy.highknowledgeapi.domain.post.domain.repository;

import com.tankboy.highknowledgeapi.domain.post.domain.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
