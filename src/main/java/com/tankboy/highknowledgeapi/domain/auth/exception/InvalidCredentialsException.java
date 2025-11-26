package com.tankboy.highknowledgeapi.domain.auth.exception;

import com.tankboy.highknowledgeapi.global.exception.CustomException;

public class InvalidCredentialsException extends CustomException {

    public InvalidCredentialsException() {
        super(AuthErrorCode.INVALID_CREDENTIALS);
    }

}
