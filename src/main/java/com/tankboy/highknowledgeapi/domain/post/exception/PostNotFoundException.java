package com.tankboy.highknowledgeapi.domain.post.exception;

import com.tankboy.highknowledgeapi.global.exception.CustomException;

public class PostNotFoundException extends CustomException {

    public PostNotFoundException() {
        super(PostErrorCode.POST_NOT_FOUND);
    }

}
