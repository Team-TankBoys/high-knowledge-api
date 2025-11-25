package com.tankboy.highknowledgeapi.domain.post.domain.entity;

import java.util.Date;

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
public class PostEntity extends BaseEntity {

    @Column(nullable = false, updatable = false)
    private Long authorUserId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public void setContent(String content) {
        this.content = content;
    }
}
