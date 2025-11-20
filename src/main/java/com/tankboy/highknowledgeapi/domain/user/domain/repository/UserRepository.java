package com.tankboy.highknowledgeapi.domain.user.domain.repository;

import com.tankboy.highknowledgeapi.domain.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
