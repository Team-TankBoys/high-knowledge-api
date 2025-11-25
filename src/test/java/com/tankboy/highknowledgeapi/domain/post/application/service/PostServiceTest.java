package com.tankboy.highknowledgeapi.domain.post.application.service;

import com.tankboy.highknowledgeapi.domain.post.domain.entity.PostEntity;
import com.tankboy.highknowledgeapi.domain.post.domain.repository.PostRepository;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.request.CreatePostRequest;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.request.UpdatePostRequest;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.response.PostResponse;
import com.tankboy.highknowledgeapi.domain.user.domain.entity.UserEntity;
import com.tankboy.highknowledgeapi.domain.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostService 테스트")
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private PostService postService;

    private UserEntity testUser;
    private PostEntity testPost;
    private final String TEST_USERNAME = "testUser";
    private final Long TEST_USER_ID = 1L;
    private final Long TEST_POST_ID = 1L;

    @BeforeEach
    void setUp() {
        // 테스트 사용자 설정
        testUser = UserEntity.builder()
                .id(TEST_USER_ID)
                .name(TEST_USERNAME)
                .build();

        // 테스트 게시글 설정
        testPost = PostEntity.builder()
                .id(TEST_POST_ID)
                .authorUserId(TEST_USER_ID)
                .title("Test Title")
                .content("Test Content")
                .build();

        // SecurityContext 모킹
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("게시글 생성 성공")
    void create_Success() {
        // given
        CreatePostRequest request = new CreatePostRequest("New Title", "New Content");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(TEST_USERNAME, null);

        given(securityContext.getAuthentication()).willReturn(authentication);
        given(userRepository.findByName(TEST_USERNAME)).willReturn(Optional.of(testUser));
        given(postRepository.save(any(PostEntity.class))).willReturn(testPost);

        // when
        PostResponse response = postService.create(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("New Title");
        assertThat(response.content()).isEqualTo("New Content");
        verify(userRepository, times(1)).findByName(TEST_USERNAME);
        verify(postRepository, times(1)).save(any(PostEntity.class));
    }

    @Test
    @DisplayName("게시글 생성 실패 - 사용자를 찾을 수 없음")
    void create_Fail_UserNotFound() {
        // given
        CreatePostRequest request = new CreatePostRequest("New Title", "New Content");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(TEST_USERNAME, null);

        given(securityContext.getAuthentication()).willReturn(authentication);
        given(userRepository.findByName(TEST_USERNAME)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found with name: " + TEST_USERNAME);

        verify(userRepository, times(1)).findByName(TEST_USERNAME);
        verify(postRepository, never()).save(any(PostEntity.class));
    }

    @Test
    @DisplayName("ID로 게시글 조회 성공")
    void findById_Success() {
        // given
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(testPost));

        // when
        PostResponse response = postService.findById(TEST_POST_ID);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(TEST_POST_ID);
        assertThat(response.title()).isEqualTo("Test Title");
        assertThat(response.content()).isEqualTo("Test Content");
        verify(postRepository, times(1)).findById(TEST_POST_ID);
    }

    @Test
    @DisplayName("ID로 게시글 조회 실패 - 게시글을 찾을 수 없음")
    void findById_Fail_PostNotFound() {
        // given
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.findById(TEST_POST_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Post not found with id: " + TEST_POST_ID);

        verify(postRepository, times(1)).findById(TEST_POST_ID);
    }

    @Test
    @DisplayName("모든 게시글 조회 성공")
    void findAll_Success() {
        // given
        PostEntity post2 = PostEntity.builder()
                .id(2L)
                .authorUserId(TEST_USER_ID)
                .title("Test Title 2")
                .content("Test Content 2")
                .build();

        List<PostEntity> posts = List.of(testPost, post2);
        given(postRepository.findAll()).willReturn(posts);

        // when
        List<PostResponse> responses = postService.findAll();

        // then
        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).title()).isEqualTo("Test Title");
        assertThat(responses.get(1).title()).isEqualTo("Test Title 2");
        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("모든 게시글 조회 - 빈 리스트")
    void findAll_EmptyList() {
        // given
        given(postRepository.findAll()).willReturn(List.of());

        // when
        List<PostResponse> responses = postService.findAll();

        // then
        assertThat(responses).isNotNull();
        assertThat(responses).isEmpty();
        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void update_Success() {
        // given
        UpdatePostRequest request = new UpdatePostRequest("Updated Content");
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(testPost));

        // when
        PostResponse response = postService.update(TEST_POST_ID, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.content()).isEqualTo("Updated Content");  // 변경된 내용 검증
        verify(postRepository, times(1)).findById(TEST_POST_ID);
    }

    @Test
    @DisplayName("게시글 수정 실패 - 게시글을 찾을 수 없음")
    void update_Fail_PostNotFound() {
        // given
        UpdatePostRequest request = new UpdatePostRequest("Updated Content");
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.update(TEST_POST_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Post not found with id: " + TEST_POST_ID);

        verify(postRepository, times(1)).findById(TEST_POST_ID);
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void delete_Success() {
        // given
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(TEST_USERNAME, null);

        given(securityContext.getAuthentication()).willReturn(authentication);
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(testPost));
        given(userRepository.findByName(TEST_USERNAME)).willReturn(Optional.of(testUser));
        doNothing().when(postRepository).delete(testPost);

        // when
        PostResponse response = postService.delete(TEST_POST_ID);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(TEST_POST_ID);
        verify(postRepository, times(1)).findById(TEST_POST_ID);
        verify(userRepository, times(1)).findByName(TEST_USERNAME);
        verify(postRepository, times(1)).delete(testPost);
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 게시글을 찾을 수 없음")
    void delete_Fail_PostNotFound() {
        // given
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.delete(TEST_POST_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Post not found with id: " + TEST_POST_ID);

        verify(postRepository, times(1)).findById(TEST_POST_ID);
        verify(postRepository, never()).delete(any(PostEntity.class));
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 사용자를 찾을 수 없음")
    void delete_Fail_UserNotFound() {
        // given
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(TEST_USERNAME, null);

        given(securityContext.getAuthentication()).willReturn(authentication);
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(testPost));
        given(userRepository.findByName(TEST_USERNAME)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.delete(TEST_POST_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found with name: " + TEST_USERNAME);

        verify(postRepository, times(1)).findById(TEST_POST_ID);
        verify(userRepository, times(1)).findByName(TEST_USERNAME);
        verify(postRepository, never()).delete(any(PostEntity.class));
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 권한 없음")
    void delete_Fail_Unauthorized() {
        // given
        UserEntity anotherUser = UserEntity.builder()
                .id(2L)
                .name("anotherUser")
                .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("anotherUser", null);

        given(securityContext.getAuthentication()).willReturn(authentication);
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(testPost));
        given(userRepository.findByName("anotherUser")).willReturn(Optional.of(anotherUser));

        // when & then
        assertThatThrownBy(() -> postService.delete(TEST_POST_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("You are not authorized to delete this post.");

        verify(postRepository, times(1)).findById(TEST_POST_ID);
        verify(userRepository, times(1)).findByName("anotherUser");
        verify(postRepository, never()).delete(any(PostEntity.class));
    }
}