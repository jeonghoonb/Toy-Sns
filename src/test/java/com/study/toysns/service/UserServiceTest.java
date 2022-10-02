package com.study.toysns.service;

import com.study.toysns.exception.ErrorCode;
import com.study.toysns.exception.SnsApplicationException;
import com.study.toysns.fixture.UserEntityFixture;
import com.study.toysns.model.entity.UserEntity;
import com.study.toysns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("UserService 테스트 - 회원가입, 로그인")
@SpringBootTest
class UserServiceTest {

    private final UserService userService;

    public UserServiceTest(@Autowired UserService userService) {
        this.userService = userService;
    }

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;


    @DisplayName("회원 가입 테스트 - 정상 케이스")
    @Test
    void  givenUserInfo_whenTryingToJoin_thenReturnIsOk() {
        // given
        String userName = "userName";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(userName, password, 1L);

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(userName, password, 1L));

        // then
        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @DisplayName("회원 가입 테스트 - userName 중복으로 인한 실패 케이스")
    @Test
    void  givenUserInfo_whenTryingToJoin_thenThrowSnsApplicationException() {
        // given
        String userName = "userName";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(userName, password, 1L);

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        // then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(userName, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
    }

    @DisplayName("로그인 테스트 - 정상 케이스")
    @Test
    void  givenUserInfo_whenTryingToLogin_thenReturnIsOk() {
        // given
        String userName = "userName";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(userName, password, 1L);

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        // then
        Assertions.assertDoesNotThrow(() -> userService.login(userName, password));
    }

    @DisplayName("로그인 테스트 - 회원 정보가 없는 userName으로 인한 실패 케이스")
    @Test
    void  givenUserInfo_whenTryingToLogin_thenThrowSnsApplicationException() {
        // given
        String userName = "userName";
        String password = "password";

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        // then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(userName, password));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @DisplayName("로그인 테스트 - 틀린 password로 인한 실패 케이스")
    @Test
    void  givenWrongUserInfo_whenTryingToLogin_thenThrowSnsApplicationException() {
        // given
        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";
        UserEntity fixture = UserEntityFixture.get(userName, password, 2L);

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        // then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(userName, wrongPassword));
        Assertions.assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, e.getErrorCode());
    }
}