package com.tankboy.highknowledgeapi.domain.user.application.service;

import com.tankboy.highknowledgeapi.domain.user.domain.entity.UserEntity;
import com.tankboy.highknowledgeapi.domain.user.domain.repository.UserRepository;
import com.tankboy.highknowledgeapi.domain.user.presentation.dto.request.RegisterUserRequest;
import com.tankboy.highknowledgeapi.domain.user.presentation.dto.response.UserMeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserMeResponse create(RegisterUserRequest request) {
        UserEntity user = UserEntity.builder()
                .name(request.name())
                .password(request.password())
                .email(request.email())
                .build();
        userRepository.save(user);

        return UserMeResponse.of(user);
    }

}
