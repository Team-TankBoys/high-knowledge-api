package com.tankboy.highknowledgeapi.domain.auth.exception;

import com.tankboy.highknowledgeapi.global.exception.CustomException;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException() {
        super(AuthErrorCode.USER_NOT_FOUND);
    }

}
