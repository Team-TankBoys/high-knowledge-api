package com.tankboy.highknowledgeapi.domain.auth.exception;

import com.tankboy.highknowledgeapi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 사용자"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "잘못된 인증 정보"),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증 실패");

    private final HttpStatus httpStatus;
    private final String message;

}
