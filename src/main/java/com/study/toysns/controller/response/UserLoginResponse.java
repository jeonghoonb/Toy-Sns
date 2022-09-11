package com.study.toysns.controller.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginResponse {

    private String token;

    public static UserLoginResponse of(String token) {
        return new UserLoginResponse(token);
    }

}
