package com.tankboy.highknowledgeapi.domain.user.exception;

import com.tankboy.highknowledgeapi.global.exception.CustomException;

public class UserAlreadyExistsException extends CustomException {

    public UserAlreadyExistsException() {
        super(UserErrorCode.USER_ALREADY_EXISTS);
    }

}
