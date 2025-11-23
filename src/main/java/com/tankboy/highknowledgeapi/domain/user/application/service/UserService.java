package com.tankboy.highknowledgeapi.domain.user.application.service;

import ch.qos.logback.core.util.StringUtil;
import com.tankboy.highknowledgeapi.domain.user.domain.entity.UserEntity;
import com.tankboy.highknowledgeapi.domain.user.domain.repository.UserRepository;
import com.tankboy.highknowledgeapi.domain.user.presentation.dto.request.RegisterUserRequest;
import com.tankboy.highknowledgeapi.domain.user.presentation.dto.request.UpdateUserRequest;
import com.tankboy.highknowledgeapi.domain.user.presentation.dto.response.UserMeResponse;
import com.tankboy.highknowledgeapi.domain.user.presentation.dto.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserMeResponse create(RegisterUserRequest request) {
        UserEntity user = UserEntity.builder()
                .name(request.name())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .build();
        userRepository.save(user);

        return UserMeResponse.of(user);
    }

    @Transactional
    public UserProfileResponse findById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserProfileResponse.of(user);
    }

    @Transactional
    public List<UserProfileResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserProfileResponse::of)
                .toList();
    }

    @Transactional
    public UserMeResponse update(Long id, UpdateUserRequest request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setName(request.name());
        user.setEmail(request.email());
        if (!StringUtil.isNullOrEmpty(request.password())) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        userRepository.save(user);

        return UserMeResponse.of(user);
    }

    @Transactional
    public UserMeResponse delete(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
        return UserMeResponse.of(user);
    }

}
