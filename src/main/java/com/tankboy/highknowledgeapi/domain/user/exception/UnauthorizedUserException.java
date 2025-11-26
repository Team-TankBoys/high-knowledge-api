package com.tankboy.highknowledgeapi.domain.user.exception;

import com.tankboy.highknowledgeapi.global.exception.CustomException;

public class UnauthorizedUserException extends CustomException {

    public UnauthorizedUserException() {
        super(UserErrorCode.UNAUTHORIZED_USER);
    }

}
