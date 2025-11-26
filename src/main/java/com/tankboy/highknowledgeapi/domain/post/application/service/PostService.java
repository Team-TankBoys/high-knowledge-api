package com.tankboy.highknowledgeapi.domain.post.application.service;

import com.tankboy.highknowledgeapi.domain.post.presentation.dto.request.CreatePostRequest;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.request.UpdatePostRequest;
import com.tankboy.highknowledgeapi.domain.post.presentation.dto.response.PostResponse;

import java.util.List;

public interface PostService {

    PostResponse create(CreatePostRequest request);

    PostResponse findById(Long id);

    List<PostResponse> findAll();

    PostResponse update(Long id, UpdatePostRequest request);

    PostResponse delete(Long id);

}
