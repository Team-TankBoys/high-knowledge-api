package com.tankboy.highknowledgeapi.global.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorResponse {

    private final int status;
    private final String message;

    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return ErrorResponse.builder()
                .status(httpStatus.value())
                .message(message)
                .build();
    }

}
