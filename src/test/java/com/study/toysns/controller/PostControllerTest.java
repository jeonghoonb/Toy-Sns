package com.study.toysns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.toysns.controller.request.PostCreateRequest;
import com.study.toysns.controller.request.PostModifyRequest;
import com.study.toysns.exception.ErrorCode;
import com.study.toysns.exception.SnsApplicationException;
import com.study.toysns.fixture.PostEntityFixture;
import com.study.toysns.model.Post;
import com.study.toysns.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@DisplayName("PostController Test - 게시물 작성, 수정, 삭제")
@AutoConfigureMockMvc
@SpringBootTest
public class PostControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    public PostControllerTest(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @DisplayName("게시물 작성 - 정상 케이스")
    @WithMockUser
    @Test
    void givenPostInfo_whenTryingToCreate_thenReturnIsOk() throws Exception {
        // given
        String title = "title";
        String body = "body";

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @DisplayName("게시물 작성 - 비로그인 케이스")
    @WithAnonymousUser
    @Test
    void givenPostInfo_whenTryingToCreate_thenReturnIsUnauthorized() throws Exception {
        // given
        String title = "title";
        String body = "body";

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @DisplayName("게시물 수정 - 정상 케이스")
    @WithMockUser
    @Test
    void givenPostInfo_whenTryingToModify_thenReturnIsOk() throws Exception {
        // given
        String title = "title";
        String body = "body";

        // when
        when(postService.modify(eq(title), eq(body), any(), any()))
                .thenReturn(Post.from(PostEntityFixture.get("userName", 1L, 1L)));

        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @DisplayName("게시물 수정 - 비로그인 케이스")
    @WithAnonymousUser
    @Test
    void givenPostInfoAndNotLogin_whenTryingToModify_thenReturnIsUnauthorized() throws Exception {
        // given
        String title = "title";
        String body = "body";

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @DisplayName("게시물 수정 - 게시물 작성자가 본인이 아닌 케이스")
    @WithAnonymousUser
    @Test
    void givenNotOwnerPostInfo_whenTryingToModify_thenReturnIsUnauthorized() throws Exception {
        // given
        String title = "title";
        String body = "body";

        // when
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body), any(), eq(1L));

        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @DisplayName("게시물 수정 - 해당 게시물이 존재하지 않는 케이스")
    @WithAnonymousUser
    @Test
    void givenNotFoundPostInfo_whenTryingToModify_thenReturnIsNotFound() throws Exception {
        // given
        String title = "title";
        String body = "body";

        // when
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title), eq(body), any(), eq(11L));

        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/posts/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @DisplayName("게시물 삭제 - 정상 케이스")
    @WithAnonymousUser
    @Test
    void givenPostId_whenTryingToDelete_thenReturnIsOk() throws Exception {
        // given

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("게시물 삭제 - 비로그인 케이스")
    @WithAnonymousUser
    @Test
    void givenPostIdAndNotLogin_whenTryingToDelete_thenReturnIsUnauthorized() throws Exception {
        // given

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @DisplayName("게시물 삭제 - 게시물 작성자가 본인이 아닌 케이스")
    @WithAnonymousUser
    @Test
    void givenNotOwnerPostId_whenTryingToDelete_thenReturnIsUnauthorized() throws Exception {
        // given

        // when
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @DisplayName("게시물 삭제 - 해당 게시물이 존재하지 않는 케이스")
    @WithAnonymousUser
    @Test
    void givenNotFoundPostId_whenTryingToDelete_thenReturnIsUnauthorized() throws Exception {
        // given

        // when
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}
