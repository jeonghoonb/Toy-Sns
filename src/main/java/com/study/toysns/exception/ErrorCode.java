package com.study.toysns.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User Name is duplicated");

    @Getter
    private final HttpStatus status;

    @Getter
    private final String message;
}
