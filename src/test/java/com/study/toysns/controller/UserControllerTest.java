package com.study.toysns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.toysns.controller.request.UserJoinRequest;
import com.study.toysns.controller.request.UserLoginRequest;
import com.study.toysns.exception.ErrorCode;
import com.study.toysns.exception.SnsApplicationException;
import com.study.toysns.model.User;
import com.study.toysns.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("UserController 테스트 - 회원가입, 로그인")
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public UserControllerTest(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @MockBean
    private UserService userService;

    @DisplayName("회원 가입 테스트 - 정상 케이스")
    @Test
    void givenUserInfo_whenTryingToJoin_thenReturnIsOk() throws Exception {
        // given
        String userName = "userName";
        String password = "password";

        // when
        when(userService.join(userName, password)).thenReturn(mock(User.class));

        // then
        mockMvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
        ).andDo(print())
        .andExpect(status().isOk());

    }

    @DisplayName("회원 가입 테스트 - userName 중복으로 인한 실패 케이스")
    @Test
    void givenUserInfo_whenTryingToJoin_thenReturnConflict() throws Exception {
        // given
        String userName = "userName";
        String password = "password";

        // when
        when(userService.join(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        // then
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isConflict());

    }

    @DisplayName("로그인 테스트 - 정상 케이스")
    @Test
    void givenUserInfo_whenTryingLogin_thenReturnIsOk() throws Exception {
        // given
        String userName = "userName";
        String password = "password";

        // when
        when(userService.login(userName, password)).thenReturn("test_token");

        // then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("로그인 테스트 - 회원 정보가 없는 userName으로 인한 실패 케이스")
    @Test
    void givenWrongUserInfo_whenTryingToLogin_thenReturnNotFound() throws Exception {
        // given
        String userName = "userName";
        String password = "password";

        // when
        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

        // then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());

    }

    @DisplayName("로그인 테스트 - 틀린 password로 인한 실패 케이스")
    @Test
    void givenWrongUserInfo_whenTryingToLogin_thenReturnUnauthorized() throws Exception {
        // given
        String userName = "userName";
        String password = "password";

        // when
        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.INVALID_PASSWORD));

        // then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());

    }

}
