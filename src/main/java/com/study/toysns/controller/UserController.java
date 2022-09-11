package com.study.toysns.controller;

import com.study.toysns.controller.request.UserJoinRequest;
import com.study.toysns.controller.request.UserLoginRequest;
import com.study.toysns.controller.response.Response;
import com.study.toysns.controller.response.UserJoinResponse;
import com.study.toysns.controller.response.UserLoginResponse;
import com.study.toysns.model.User;
import com.study.toysns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getUserName(), request.getPassword());

        return Response.success(UserJoinResponse.from(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUserName(), request.getPassword());

        return Response.success(UserLoginResponse.of(token));
    }
}
