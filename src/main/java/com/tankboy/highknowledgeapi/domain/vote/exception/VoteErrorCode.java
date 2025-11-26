package com.tankboy.highknowledgeapi.domain.vote.exception;

import com.tankboy.highknowledgeapi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum VoteErrorCode implements ErrorCode {

    VOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 투표"),
    DUPLICATE_VOTE(HttpStatus.CONFLICT, "중복 투표");

    private final HttpStatus httpStatus;
    private final String message;

}
