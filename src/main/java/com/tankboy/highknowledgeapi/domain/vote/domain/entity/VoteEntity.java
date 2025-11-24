package com.tankboy.highknowledgeapi.domain.vote.domain.entity;

import com.tankboy.highknowledgeapi.domain.vote.domain.enums.VoteType;
import com.tankboy.highknowledgeapi.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteEntity extends BaseEntity {

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private VoteType type;

}
