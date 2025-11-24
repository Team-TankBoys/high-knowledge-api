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

    @Column(nullable = false)
    private String title;
     
    @Column(nullable = false)
    private Date timeDate;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String password;

    public void update(String title, Date timeDate, String content, String password) {
        this.title = title;
        this.timeDate = timeDate;
        this.content = content;
        this.password = password;
    }
}
