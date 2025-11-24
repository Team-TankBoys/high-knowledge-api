package com.tankboy.highknowledgeapi.domain.post.application.service;

import com.tankboy.highknowledgeapi.domain.post.domain.entity.PostEntity;
import com.tankboy.highknowledgeapi.domain.post.domain.repository.PostRepository;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.request.CreatePostRequest;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.request.UpdatePostRequest;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        PostEntity post = PostEntity.builder()
                .title(request.title())
                .timeDate(request.timeDate())
                .content(request.content())
                .password(request.password())
                .build();
        PostEntity savedPost = postRepository.save(post);
        return new PostResponse(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getTimeDate(),
                savedPost.getContent()
        );
    }

    public PostResponse getPost(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getTimeDate(),
                post.getContent()
        );
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getTimeDate(),
                        post.getContent()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse updatePost(Long id, UpdatePostRequest request) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        post.update(request.title(), request.timeDate(), request.content(), request.password());
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getTimeDate(),
                post.getContent()
        );
    }

    @Transactional
    public PostResponse deletePost(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        postRepository.delete(post);
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getTimeDate(),
                post.getContent()
        );
    }

}
