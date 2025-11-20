package com.tankboy.highknowledgeapi.domain.user.presentation.controller;

import com.tankboy.highknowledgeapi.domain.user.application.service.UserService;
import com.tankboy.highknowledgeapi.domain.user.presentation.dto.request.RegisterUserRequest;
import com.tankboy.highknowledgeapi.domain.user.presentation.dto.response.UserMeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserMeResponse registerUser(@RequestBody RegisterUserRequest request) {
        return userService.create(request);
    }

}
