package com.study.toysns.service;

import com.study.toysns.exception.ErrorCode;
import com.study.toysns.exception.SnsApplicationException;
import com.study.toysns.model.entity.PostEntity;
import com.study.toysns.model.entity.UserEntity;
import com.study.toysns.repository.PostEntityRepository;
import com.study.toysns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("PostService Test - 게시물 작성, 수정, 삭제")
@SpringBootTest
public class PostServiceTest {

    private final PostService postService;

    @MockBean
    private PostEntityRepository postEntityRepository;

    @MockBean
    private UserEntityRepository userEntityRepository;

    public PostServiceTest(@Autowired PostService postService) {
        this.postService = postService;
    }

    @DisplayName("게시물 작성 - 정상 케이스")
    @Test
    void givenPostInfo_whenTryingToCreate_thenReturnIsOk() {
        // given
        String title = "title";
        String body = " body";
        String userName = "userName";

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        // then
        Assertions.assertDoesNotThrow(() -> postService.create(title, body, userName));
    }

    @DisplayName("게시물 작성 - 요청한 유저가 존재하지 않는 케이스")
    @Test
    void givenPostInfo_whenTryingToCreate_thenThrowSnsApplicationException() {
        // given
        String title = "title";
        String body = " body";
        String userName = "userName";

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        // then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.create(title, body, userName));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }
}
