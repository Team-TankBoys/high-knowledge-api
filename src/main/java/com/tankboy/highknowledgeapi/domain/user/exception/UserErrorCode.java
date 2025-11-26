package com.tankboy.highknowledgeapi.domain.user.exception;

import com.tankboy.highknowledgeapi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 유저"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 유저"),
    UNAUTHORIZED_USER(HttpStatus.FORBIDDEN, "권한 없는 유저");

    private final HttpStatus httpStatus;
    private final String message;

}
