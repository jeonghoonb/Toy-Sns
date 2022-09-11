package com.study.toysns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.Objects.isNull;

@Getter
@AllArgsConstructor
public class SnsApplicationException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public SnsApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    @Override
    public String getMessage() {
        if (isNull(message)) {
            return errorCode.getMessage();
        }

        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}
