package com.tankboy.highknowledgeapi.domain.vote.exception;

import com.tankboy.highknowledgeapi.global.exception.CustomException;

public class DuplicateVoteException extends CustomException {

    public DuplicateVoteException() {
        super(VoteErrorCode.DUPLICATE_VOTE);
    }

}
