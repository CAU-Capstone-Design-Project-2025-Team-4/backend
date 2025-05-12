package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.dto.user.UserLoginRequest;
import com.capstone2025.team4.backend.controller.dto.user.UserLoginResponse;
import com.capstone2025.team4.backend.controller.dto.user.UserRegisterRequest;
import com.capstone2025.team4.backend.controller.dto.user.UserRegisterResponse;
import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.security.jwt.JwtService;
import com.capstone2025.team4.backend.security.jwt.UserRefreshToken;
import com.capstone2025.team4.backend.security.jwt.UserRefreshTokenRepository;
import com.capstone2025.team4.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserRefreshTokenRepository refreshTokenRepository;

    @PostMapping("/register")
    public ApiResponse<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest request) {

        User user = userService.register(request.getName(), request.getEmail(), request.getPassword(), request.getConfirmPassword());

        return ApiResponse.success(new UserRegisterResponse(user));
    }

    @PostMapping("/login")
    public ApiResponse<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword());

        String token = jwtService.generateJwtToken(user.getEmail());

        String refreshToken = getRefreshToken(user, request.getJwtToken());

        return ApiResponse.success(new UserLoginResponse(user, token, refreshToken));
    }

    private String getRefreshToken(User user, String prevJwt) {
        return refreshTokenRepository.findByUserEmail(user.getEmail())
                .map(rt -> {                     // 이미 저장돼 있으면
                    rt.updatePrevJwt(prevJwt);   //   이전 AT 만 기록
                    return rt.getRefreshToken(); //   RT 는 재사용
                })
                .orElseGet(() -> {               // 없을 때만 새 RT 발급
                    String newRt = jwtService.generateRefreshToken();
                    refreshTokenRepository.save(
                            new UserRefreshToken(user, newRt, prevJwt));
                    return newRt;
                });
    }
}
