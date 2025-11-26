package com.tankboy.highknowledgeapi.domain.post.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tankboy.highknowledgeapi.domain.post.domain.entity.PostEntity;
import com.tankboy.highknowledgeapi.domain.post.domain.repository.PostRepository;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.request.CreatePostRequest;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.request.UpdatePostRequest;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.response.PostResponse;
import com.tankboy.highknowledgeapi.domain.user.domain.entity.UserEntity;
import com.tankboy.highknowledgeapi.domain.user.domain.repository.UserRepository;
import com.tankboy.highknowledgeapi.test.config.BaseController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("PostController 통합 테스트")
class PostControllerTest extends BaseController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity testUser;
    private UserEntity anotherUser;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성
        testUser = UserEntity.builder()
                .name("testUser")
                .password("password123")
                .build();
        testUser = userRepository.save(testUser);

        anotherUser = UserEntity.builder()
                .name("anotherUser")
                .password("password456")
                .build();
        anotherUser = userRepository.save(anotherUser);
    }

    @AfterEach
    void tearDown() {
        // 테스트 데이터 정리
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 생성 성공")
    @WithMockUser(username = "testUser")
    void createPost_Success() throws Exception {
        // given
        CreatePostRequest request = new CreatePostRequest("Test Title", "Test Content");

        // when
        ResultActions result = mockMvc.perform(post("/posts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.authorUserId").value(testUser.getId()));

        // AssertJ로 응답 본문 추가 검증
        String responseBody = result.andReturn().getResponse().getContentAsString();
        PostResponse actualResponse = objectMapper.readValue(responseBody, PostResponse.class);
        assertThat(actualResponse.title()).isEqualTo("Test Title");
        assertThat(actualResponse.content()).isEqualTo("Test Content");
        assertThat(actualResponse.authorUserId()).isEqualTo(testUser.getId());

        // 데이터베이스에 저장되었는지 확인
        List<PostEntity> posts = postRepository.findAll();
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getTitle()).isEqualTo("Test Title");
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

        // 데이터베이스에 저장되지 않았는지 확인
        List<PostEntity> posts = postRepository.findAll();
        assertThat(posts).isEmpty();
    }

    @Test
    @DisplayName("ID로 게시글 조회 성공")
    @WithMockUser
    void getPost_Success() throws Exception {
        // given
        PostEntity post = PostEntity.builder()
                .authorUserId(testUser.getId())
                .title("Test Title")
                .content("Test Content")
                .build();
        post = postRepository.save(post);

        // when
        ResultActions result = mockMvc.perform(get("/posts/{id}", post.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.authorUserId").value(testUser.getId()));

        // AssertJ 검증
        String responseBody = result.andReturn().getResponse().getContentAsString();
        PostResponse actualResponse = objectMapper.readValue(responseBody, PostResponse.class);
        assertThat(actualResponse.id()).isEqualTo(post.getId());
        assertThat(actualResponse.title()).isEqualTo("Test Title");
        assertThat(actualResponse.content()).isEqualTo("Test Content");
    }

    @Test
    @DisplayName("ID로 게시글 조회 실패 - 게시글 없음")
    @WithMockUser
    void getPost_Fail_NotFound() throws Exception {
        // given
        Long nonExistentId = 99999L;

        // when
        ResultActions result = mockMvc.perform(get("/posts/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("모든 게시글 조회 성공")
    @WithMockUser
    void getAllPosts_Success() throws Exception {
        // given
        PostEntity post1 = PostEntity.builder()
                .authorUserId(testUser.getId())
                .title("Title 1")
                .content("Content 1")
                .build();
        postRepository.save(post1);

        PostEntity post2 = PostEntity.builder()
                .authorUserId(testUser.getId())
                .title("Title 2")
                .content("Content 2")
                .build();
        postRepository.save(post2);

        // when
        ResultActions result = mockMvc.perform(get("/posts")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[1].title").value("Title 2"));

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
        // when
        ResultActions result = mockMvc.perform(get("/posts")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

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
        PostEntity post = PostEntity.builder()
                .authorUserId(testUser.getId())
                .title("Original Title")
                .content("Original Content")
                .build();
        post = postRepository.save(post);

        UpdatePostRequest request = new UpdatePostRequest("Updated Content");

        // when
        ResultActions result = mockMvc.perform(put("/posts/{id}", post.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.content").value("Updated Content"));

        // AssertJ 검증
        String responseBody = result.andReturn().getResponse().getContentAsString();
        PostResponse actualResponse = objectMapper.readValue(responseBody, PostResponse.class);
        assertThat(actualResponse.content()).isEqualTo("Updated Content");

        // 데이터베이스에서 실제로 업데이트되었는지 확인
        PostEntity updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertThat(updatedPost.getContent()).isEqualTo("Updated Content");
    }

    @Test
    @DisplayName("게시글 수정 실패 - 게시글 없음")
    @WithMockUser(username = "testUser")
    void updatePost_Fail_NotFound() throws Exception {
        // given
        Long nonExistentId = 99999L;
        UpdatePostRequest request = new UpdatePostRequest("Updated Content");

        // when
        ResultActions result = mockMvc.perform(put("/posts/{id}", nonExistentId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 권한 없음")
    @WithMockUser(username = "anotherUser")
    void updatePost_Fail_Unauthorized() throws Exception {
        // given
        PostEntity post = PostEntity.builder()
                .authorUserId(testUser.getId())
                .title("Original Title")
                .content("Original Content")
                .build();
        post = postRepository.save(post);

        UpdatePostRequest request = new UpdatePostRequest("Updated Content");

        // when
        ResultActions result = mockMvc.perform(put("/posts/{id}", post.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andDo(print())
                .andExpect(status().isForbidden());

        // 데이터베이스에서 수정되지 않았는지 확인
        PostEntity unchangedPost = postRepository.findById(post.getId()).orElseThrow();
        assertThat(unchangedPost.getContent()).isEqualTo("Original Content");
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    @WithMockUser(username = "testUser")
    void deletePost_Success() throws Exception {
        // given
        PostEntity post = PostEntity.builder()
                .authorUserId(testUser.getId())
                .title("Test Title")
                .content("Test Content")
                .build();
        post = postRepository.save(post);
        Long postId = post.getId();

        // when
        ResultActions result = mockMvc.perform(delete("/posts/{id}", postId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId));

        // AssertJ 검증
        String responseBody = result.andReturn().getResponse().getContentAsString();
        PostResponse actualResponse = objectMapper.readValue(responseBody, PostResponse.class);
        assertThat(actualResponse.id()).isEqualTo(postId);

        // 데이터베이스에서 실제로 삭제되었는지 확인
        assertThat(postRepository.findById(postId)).isEmpty();
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 게시글 없음")
    @WithMockUser(username = "testUser")
    void deletePost_Fail_NotFound() throws Exception {
        // given
        Long nonExistentId = 99999L;

        // when
        ResultActions result = mockMvc.perform(delete("/posts/{id}", nonExistentId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 권한 없음")
    @WithMockUser(username = "anotherUser")
    void deletePost_Fail_Unauthorized() throws Exception {
        // given
        PostEntity post = PostEntity.builder()
                .authorUserId(testUser.getId())
                .title("Test Title")
                .content("Test Content")
                .build();
        post = postRepository.save(post);

        // when
        ResultActions result = mockMvc.perform(delete("/posts/{id}", post.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isForbidden());

        // 데이터베이스에서 삭제되지 않았는지 확인
        assertThat(postRepository.findById(post.getId())).isPresent();
    }
}
