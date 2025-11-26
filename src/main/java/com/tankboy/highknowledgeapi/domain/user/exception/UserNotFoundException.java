package com.tankboy.highknowledgeapi.domain.user.exception;

import com.tankboy.highknowledgeapi.global.exception.CustomException;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }

}
