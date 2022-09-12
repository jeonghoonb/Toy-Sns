package com.study.toysns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.toysns.controller.request.PostCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DisplayName("PostController Test - 게시물 작성, 수정, 삭제")
@AutoConfigureMockMvc
@SpringBootTest
public class PostControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

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
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/post")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }
}
