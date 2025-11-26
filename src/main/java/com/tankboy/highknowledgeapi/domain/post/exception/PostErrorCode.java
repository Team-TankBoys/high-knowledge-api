package com.tankboy.highknowledgeapi.domain.post.exception;

import com.tankboy.highknowledgeapi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 게시글"),
    UNAUTHORIZED_POST_ACCESS(HttpStatus.FORBIDDEN, "권한 없는 게시글 접근");

    private final HttpStatus httpStatus;
    private final String message;

}
