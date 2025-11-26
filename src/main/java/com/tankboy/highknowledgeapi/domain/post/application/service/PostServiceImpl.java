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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse create(CreatePostRequest request) {
        String username = (String)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity userEntity = userRepository.findByName(username)
                .orElseThrow(UserNotFoundException::new);

        PostEntity post = PostEntity.builder()
                .authorUserId(userEntity.getId())
                .title(request.title())
                .content(request.content())
                .build();
        postRepository.save(post);

        return PostResponse.of(post);
    }

    public PostResponse findById(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        return PostResponse.of(post);
    }

    public List<PostResponse> findAll() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::of)
                .toList();
    }

    @Transactional
    public PostResponse update(Long id, UpdatePostRequest request) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        post.setContent(request.content());
        return PostResponse.of(post);
    }

    @Transactional
    public PostResponse delete(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        String username = (String)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity userEntity = userRepository.findByName(username)
                .orElseThrow(UserNotFoundException::new);

        if (post.getAuthorUserId() != userEntity.getId()) {
            throw new UnauthorizedPostAccessException();
        }

        postRepository.delete(post);
        return PostResponse.of(post);
    }

}
