package com.tankboy.highknowledgeapi.domain.post.application.service;

import com.tankboy.highknowledgeapi.domain.post.domain.entity.PostEntity;
import com.tankboy.highknowledgeapi.domain.post.domain.repository.PostRepository;
import com.tankboy.highknowledgeapi.domain.post.exception.PostNotFoundException;
import com.tankboy.highknowledgeapi.domain.post.exception.UnauthorizedPostAccessException;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.request.CreatePostRequest;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.request.UpdatePostRequest;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.response.PostResponse;
import com.tankboy.highknowledgeapi.domain.user.domain.entity.UserEntity;
import com.tankboy.highknowledgeapi.domain.user.domain.repository.UserRepository;
import com.tankboy.highknowledgeapi.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PostServiceImpl postService;

    private UserEntity testUser;
    private PostEntity testPost;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성
        testUser = UserEntity.builder()
                .id(1L)
                .name("testUser")
                .build();

        // 테스트용 게시글 생성
        testPost = PostEntity.builder()
                .id(1L)
                .authorUserId(testUser.getId())
                .title("테스트 제목")
                .content("테스트 내용")
                .build();

        // SecurityContext Mock 설정
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
        CreatePostRequest request = new CreatePostRequest("새 게시글 제목", "새 게시글 내용");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("testUser");
        when(userRepository.findByName("testUser")).thenReturn(Optional.of(testUser));
        when(postRepository.save(any(PostEntity.class))).thenReturn(testPost);

        // when
        PostResponse result = postService.create(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.authorUserId()).isEqualTo(testUser.getId());
        verify(postRepository, times(1)).save(any(PostEntity.class));
        verify(userRepository, times(1)).findByName("testUser");
    }

    @Test
    @DisplayName("게시글 생성 실패 - 사용자를 찾을 수 없음")
    void create_Fail_UserNotFound() {
        // given
        CreatePostRequest request = new CreatePostRequest("새 게시글 제목", "새 게시글 내용");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("unknownUser");
        when(userRepository.findByName("unknownUser")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.create(request))
                .isInstanceOf(UserNotFoundException.class);

        verify(postRepository, times(0)).save(any(PostEntity.class));
    }

    @Test
    @DisplayName("ID로 게시글 조회 성공")
    void findById_Success() {
        // given
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));

        // when
        PostResponse result = postService.findById(postId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(postId);
        assertThat(result.title()).isEqualTo("테스트 제목");
        assertThat(result.content()).isEqualTo("테스트 내용");
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("ID로 게시글 조회 실패 - 게시글을 찾을 수 없음")
    void findById_Fail_PostNotFound() {
        // given
        Long postId = 999L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.findById(postId))
                .isInstanceOf(PostNotFoundException.class);

        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("모든 게시글 조회 성공")
    void findAll_Success() {
        // given
        PostEntity post2 = PostEntity.builder()
                .id(2L)
                .authorUserId(testUser.getId())
                .title("테스트 제목 2")
                .content("테스트 내용 2")
                .build();

        List<PostEntity> posts = List.of(testPost, post2);
        when(postRepository.findAll()).thenReturn(posts);

        // when
        List<PostResponse> results = postService.findAll();

        // then
        assertThat(results).isNotNull();
        assertThat(results).hasSize(2);
        assertThat(results.get(0).title()).isEqualTo("테스트 제목");
        assertThat(results.get(1).title()).isEqualTo("테스트 제목 2");
        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("모든 게시글 조회 - 빈 리스트")
    void findAll_EmptyList() {
        // given
        when(postRepository.findAll()).thenReturn(List.of());

        // when
        List<PostResponse> results = postService.findAll();

        // then
        assertThat(results).isNotNull();
        assertThat(results).isEmpty();
        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void update_Success() {
        // given
        Long postId = 1L;
        UpdatePostRequest request = new UpdatePostRequest("수정된 내용");

        when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));

        // when
        PostResponse result = postService.update(postId, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.content()).isEqualTo("수정된 내용");
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("게시글 수정 실패 - 게시글을 찾을 수 없음")
    void update_Fail_PostNotFound() {
        // given
        Long postId = 999L;
        UpdatePostRequest request = new UpdatePostRequest("수정된 내용");

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.update(postId, request))
                .isInstanceOf(PostNotFoundException.class);

        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void delete_Success() {
        // given
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("testUser");
        when(userRepository.findByName("testUser")).thenReturn(Optional.of(testUser));

        // when
        PostResponse result = postService.delete(postId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(postId);
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).delete(testPost);
        verify(userRepository, times(1)).findByName("testUser");
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 게시글을 찾을 수 없음")
    void delete_Fail_PostNotFound() {
        // given
        Long postId = 999L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.delete(postId))
                .isInstanceOf(PostNotFoundException.class);

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(0)).delete(any(PostEntity.class));
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 사용자를 찾을 수 없음")
    void delete_Fail_UserNotFound() {
        // given
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("unknownUser");
        when(userRepository.findByName("unknownUser")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.delete(postId))
                .isInstanceOf(UserNotFoundException.class);

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(0)).delete(any(PostEntity.class));
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 권한이 없음")
    void delete_Fail_Unauthorized() {
        // given
        Long postId = 1L;
        UserEntity anotherUser = UserEntity.builder()
                .id(2L)
                .name("anotherUser")
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("anotherUser");
        when(userRepository.findByName("anotherUser")).thenReturn(Optional.of(anotherUser));

        // when & then
        assertThatThrownBy(() -> postService.delete(postId))
                .isInstanceOf(UnauthorizedPostAccessException.class);

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(0)).delete(any(PostEntity.class));
    }
}