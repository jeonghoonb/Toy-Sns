package com.study.toysns.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.util.Objects.isNull;

@Getter
@RequiredArgsConstructor
public class SnsApplicationException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    @Override
    public String getMessage() {
        if (isNull(message)) {
            return errorCode.getMessage();
        }

        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}
