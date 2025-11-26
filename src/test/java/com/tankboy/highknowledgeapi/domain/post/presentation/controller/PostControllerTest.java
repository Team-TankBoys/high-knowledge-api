package com.tankboy.highknowledgeapi.domain.post.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tankboy.highknowledgeapi.test.config.BaseController;
import com.tankboy.highknowledgeapi.domain.post.application.service.PostService;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.request.CreatePostRequest;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.request.UpdatePostRequest;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.response.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("PostController 테스트")
class PostControllerTest extends BaseController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;

    private final Long TEST_POST_ID = 1L;
    private final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("게시글 생성 성공")
    @WithMockUser(username = "testUser")
    void createPost_Success() throws Exception {
        // given
        CreatePostRequest request = new CreatePostRequest("Test Title", "Test Content");
        PostResponse response = new PostResponse(
                TEST_POST_ID,
                TEST_USER_ID,
                "Test Title",
                "Test Content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(postService.create(any(CreatePostRequest.class))).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(post("/posts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_POST_ID))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.authorUserId").value(TEST_USER_ID));

        verify(postService, times(1)).create(any(CreatePostRequest.class));

        // AssertJ로 응답 본문 추가 검증
        String responseBody = result.andReturn().getResponse().getContentAsString();
        PostResponse actualResponse = objectMapper.readValue(responseBody, PostResponse.class);
        assertThat(actualResponse.id()).isEqualTo(TEST_POST_ID);
        assertThat(actualResponse.title()).isEqualTo("Test Title");
    }

    @Test
    @DisplayName("게시글 생성 실패 - 요청 본문 없음")
    @WithMockUser(username = "testUser")
    void createPost_Fail_NoRequestBody() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/posts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isBadRequest());

        verify(postService, times(0)).create(any(CreatePostRequest.class));
    }

    @Test
    @DisplayName("ID로 게시글 조회 성공")
    @WithMockUser
    void getPost_Success() throws Exception {
        // given
        PostResponse response = new PostResponse(
                TEST_POST_ID,
                TEST_USER_ID,
                "Test Title",
                "Test Content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(postService.findById(TEST_POST_ID)).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(get("/posts/{id}", TEST_POST_ID)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_POST_ID))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"));

        verify(postService, times(1)).findById(TEST_POST_ID);

        // AssertJ 검증
        String responseBody = result.andReturn().getResponse().getContentAsString();
        assertThat(responseBody).contains("Test Title");
        assertThat(responseBody).contains("Test Content");
    }

    @Test
    @DisplayName("ID로 게시글 조회 실패 - 게시글 없음")
    @WithMockUser
    void getPost_Fail_NotFound() throws Exception {
        // given
        given(postService.findById(TEST_POST_ID))
                .willThrow(new IllegalArgumentException("Post not found with id: " + TEST_POST_ID));

        // when
        ResultActions result = mockMvc.perform(get("/posts/{id}", TEST_POST_ID)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().is5xxServerError());

        verify(postService, times(1)).findById(TEST_POST_ID);
    }

    @Test
    @DisplayName("모든 게시글 조회 성공")
    @WithMockUser
    void getAllPosts_Success() throws Exception {
        // given
        PostResponse post1 = new PostResponse(
                1L,
                TEST_USER_ID,
                "Title 1",
                "Content 1",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PostResponse post2 = new PostResponse(
                2L,
                TEST_USER_ID,
                "Title 2",
                "Content 2",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        List<PostResponse> responses = List.of(post1, post2);
        given(postService.findAll()).willReturn(responses);

        // when
        ResultActions result = mockMvc.perform(get("/posts")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Title 2"));

        verify(postService, times(1)).findAll();

        // AssertJ로 리스트 검증
        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<PostResponse> actualResponses = objectMapper.readValue(
                responseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, PostResponse.class)
        );
        assertThat(actualResponses).hasSize(2);
        assertThat(actualResponses.get(0).title()).isEqualTo("Title 1");
        assertThat(actualResponses.get(1).title()).isEqualTo("Title 2");
    }

    @Test
    @DisplayName("모든 게시글 조회 - 빈 리스트")
    @WithMockUser
    void getAllPosts_EmptyList() throws Exception {
        // given
        given(postService.findAll()).willReturn(List.of());

        // when
        ResultActions result = mockMvc.perform(get("/posts")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(postService, times(1)).findAll();

        // AssertJ 검증
        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<PostResponse> actualResponses = objectMapper.readValue(
                responseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, PostResponse.class)
        );
        assertThat(actualResponses).isEmpty();
    }

    @Test
    @DisplayName("게시글 수정 성공")
    @WithMockUser(username = "testUser")
    void updatePost_Success() throws Exception {
        // given
        UpdatePostRequest request = new UpdatePostRequest("Updated Content");
        PostResponse response = new PostResponse(
                TEST_POST_ID,
                TEST_USER_ID,
                "Test Title",
                "Updated Content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(postService.update(eq(TEST_POST_ID), any(UpdatePostRequest.class))).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(put("/posts/{id}", TEST_POST_ID)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_POST_ID))
                .andExpect(jsonPath("$.content").value("Updated Content"));

        verify(postService, times(1)).update(eq(TEST_POST_ID), any(UpdatePostRequest.class));

        // AssertJ 검증
        String responseBody = result.andReturn().getResponse().getContentAsString();
        PostResponse actualResponse = objectMapper.readValue(responseBody, PostResponse.class);
        assertThat(actualResponse.content()).isEqualTo("Updated Content");
    }

    @Test
    @DisplayName("게시글 수정 실패 - 게시글 없음")
    @WithMockUser(username = "testUser")
    void updatePost_Fail_NotFound() throws Exception {
        // given
        UpdatePostRequest request = new UpdatePostRequest("Updated Content");
        given(postService.update(eq(TEST_POST_ID), any(UpdatePostRequest.class)))
                .willThrow(new IllegalArgumentException("Post not found with id: " + TEST_POST_ID));

        // when
        ResultActions result = mockMvc.perform(put("/posts/{id}", TEST_POST_ID)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andDo(print())
                .andExpect(status().is5xxServerError());

        verify(postService, times(1)).update(eq(TEST_POST_ID), any(UpdatePostRequest.class));
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    @WithMockUser(username = "testUser")
    void deletePost_Success() throws Exception {
        // given
        PostResponse response = new PostResponse(
                TEST_POST_ID,
                TEST_USER_ID,
                "Test Title",
                "Test Content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(postService.delete(TEST_POST_ID)).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(delete("/posts/{id}", TEST_POST_ID)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_POST_ID));

        verify(postService, times(1)).delete(TEST_POST_ID);

        // AssertJ 검증
        String responseBody = result.andReturn().getResponse().getContentAsString();
        assertThat(responseBody).isNotEmpty();
        PostResponse actualResponse = objectMapper.readValue(responseBody, PostResponse.class);
        assertThat(actualResponse.id()).isEqualTo(TEST_POST_ID);
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 권한 없음")
    @WithMockUser(username = "anotherUser")
    void deletePost_Fail_Unauthorized() throws Exception {
        // given
        given(postService.delete(TEST_POST_ID))
                .willThrow(new IllegalArgumentException("You are not authorized to delete this post."));

        // when
        ResultActions result = mockMvc.perform(delete("/posts/{id}", TEST_POST_ID)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().is5xxServerError());

        verify(postService, times(1)).delete(TEST_POST_ID);
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 인증 없음")
    void deletePost_Fail_NoAuthentication() throws Exception {
        // when
        ResultActions result = mockMvc.perform(delete("/posts/{id}", TEST_POST_ID)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isUnauthorized());

        verify(postService, times(0)).delete(TEST_POST_ID);
    }
}