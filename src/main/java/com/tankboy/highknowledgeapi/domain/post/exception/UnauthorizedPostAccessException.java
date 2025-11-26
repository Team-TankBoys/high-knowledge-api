package com.tankboy.highknowledgeapi.domain.post.exception;

import com.tankboy.highknowledgeapi.global.exception.CustomException;

public class UnauthorizedPostAccessException extends CustomException {

    public UnauthorizedPostAccessException() {
        super(PostErrorCode.UNAUTHORIZED_POST_ACCESS);
    }

}
