package com.tankboy.highknowledgeapi.domain.user.domain.repository;

import com.tankboy.highknowledgeapi.domain.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByName(String name);

    boolean existsByName(String name);

}
