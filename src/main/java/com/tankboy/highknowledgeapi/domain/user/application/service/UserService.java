package com.tankboy.highknowledgeapi.domain.user.application.service;

import com.tankboy.highknowledgeapi.domain.user.presentation.dto.request.RegisterUserRequest;
import com.tankboy.highknowledgeapi.domain.user.presentation.dto.request.UpdateUserRequest;
import com.tankboy.highknowledgeapi.domain.user.presentation.dto.response.UserMeResponse;
import com.tankboy.highknowledgeapi.domain.user.presentation.dto.response.UserProfileResponse;

import java.util.List;

public interface UserService {

    UserMeResponse create(RegisterUserRequest request);

    UserProfileResponse findById(Long id);

    List<UserProfileResponse> findAll();

    UserMeResponse update(Long id, UpdateUserRequest request);

    UserMeResponse delete(Long id);

}
