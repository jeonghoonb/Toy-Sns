package com.study.toysns.controller.response;

import com.study.toysns.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String userName;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername()
        );
    }
}
