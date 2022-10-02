package com.study.toysns.service;

import com.study.toysns.exception.ErrorCode;
import com.study.toysns.exception.SnsApplicationException;
import com.study.toysns.fixture.PostEntityFixture;
import com.study.toysns.fixture.UserEntityFixture;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    void givenPostInfo_whenTryingToCreate_thenThrowUserNotFound() {
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

    @DisplayName("게시물 수정 - 정상 케이스")
    @Test
    void givenPostInfo_whenTryingToModify_thenReturnIsOk() {
        // given
        String title = "title";
        String body = " body";
        String userName = "userName";
        Long userId = 1L;
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, userId, postId);
        UserEntity userEntity = postEntity.getUser();

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postEntityRepository.save(any())).thenReturn(postEntity);

        // then
        Assertions.assertDoesNotThrow(() -> postService.modify(title, body, userName, postId));
    }

    @DisplayName("게시물 수정 - 게시물이 존재하지 않는 케이스")
    @Test
    void givenPostInfo_whenTryingToModify_thenThrowPosttNotFound() {
        // given
        String title = "title";
        String body = " body";
        String userName = "userName";
        Long userId = 1L;
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, userId, postId);
        UserEntity userEntity = postEntity.getUser();

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        // then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @DisplayName("게시물 수정 - 게시물 작성자가 본인이 아닌 케이스")
    @Test
    void givenPostInfo_whenTryingToModifyAndNotOwner_thenThrowInvalidPermission() {
        // given
        String title = "title";
        String body = " body";
        String userName = "userName";
        Long userId = 1L;
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, userId, postId);
        UserEntity otherUserEntity = UserEntityFixture.get("therUser", "password", 2L);

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(otherUserEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @DisplayName("게시물 삭제 - 정상 케이스")
    @Test
    void givenPostId_whenTryingToDelete_thenReturnIsOk() {
        // given
        String userName = "userName";
        Long userId = 1L;
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, userId, postId);
        UserEntity userEntity = postEntity.getUser();

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // then
        Assertions.assertDoesNotThrow(() -> postService.delete(userName, postId));
    }

    @DisplayName("게시물 삭제 - 게시물이 존재하지 않는 케이스")
    @Test
    void givenNotFoubndPostId_whenTryingToDelete_thenThrowPostNotFound() {
        // given
        String userName = "userName";
        Long userId = 1L;
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, userId, postId);
        UserEntity userEntity = postEntity.getUser();

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        // then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete(userName, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @DisplayName("게시물 삭제 - 게시물 작성자가 본인이 아닌 케이스")
    @Test
    void givenNotOwnerPostId_whenTryingToDelete_thenThrowInvalidPermission() {
        // given
        String userName = "userName";
        Long userId = 1L;
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, userId, postId);
        UserEntity otherUserEntity = UserEntityFixture.get("therUser", "password", 2L);

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(otherUserEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete(userName, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @DisplayName("게시물 목록 조회 - 정상 케이스")
    @Test
    void givenNothing_whenTryingToGetList_thenReturnIsOk() {
        // given
        Pageable pageable = mock(Pageable.class);

        // when
        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());

        // then
        Assertions.assertDoesNotThrow(() -> postService.list(pageable));
    }

    @DisplayName("내 피드 목록 조회 - 정상 케이스")
    @Test
    void givenUserInfo_whenTryingToGetMyList_thenReturnIsOk() {
        // given
        UserEntity user = mock(UserEntity.class);
        Pageable pageable = mock(Pageable.class);

        // when
        when(userEntityRepository.findByUserName(any())).thenReturn(Optional.of(user));
        when(postEntityRepository.findAllByUser(user, pageable)).thenReturn(Page.empty());

        // then
        Assertions.assertDoesNotThrow(() -> postService.my(user.getUserName(), pageable));
    }

}
