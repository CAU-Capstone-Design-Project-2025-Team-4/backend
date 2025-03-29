package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.dto.user.LoginRequest;
import com.capstone2025.team4.backend.controller.dto.user.UserLoginResponse;
import com.capstone2025.team4.backend.controller.dto.user.UserRegisterRequest;
import com.capstone2025.team4.backend.controller.dto.user.UserRegisterResponse;
import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.infra.security.jwt.JwtService;
import com.capstone2025.team4.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest request) {

        User user = userService.register(request.getName(), request.getEmail(), request.getPassword(), request.getConfirmPassword());

        return ApiResponse.success(new UserRegisterResponse(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword());

        String token = jwtService.generateToken(user.getEmail());
        return ApiResponse.success(new UserLoginResponse(user, token));
    }
}
