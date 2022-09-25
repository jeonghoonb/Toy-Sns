package com.study.toysns.controller.response;

import com.study.toysns.model.User;
import com.study.toysns.model.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserJoinResponse {

    private Long id;
    private String userName;
    private UserRole role;

    public static UserJoinResponse from(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

}
