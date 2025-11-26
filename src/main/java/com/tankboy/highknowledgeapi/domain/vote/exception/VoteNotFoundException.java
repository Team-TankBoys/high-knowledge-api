package com.tankboy.highknowledgeapi.domain.vote.exception;

import com.tankboy.highknowledgeapi.global.exception.CustomException;

public class VoteNotFoundException extends CustomException {

    public VoteNotFoundException() {
        super(VoteErrorCode.VOTE_NOT_FOUND);
    }

}
