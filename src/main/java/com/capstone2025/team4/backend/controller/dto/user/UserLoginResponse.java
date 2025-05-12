package com.capstone2025.team4.backend.controller.dto.user;

import com.capstone2025.team4.backend.domain.User;
import lombok.Getter;

@Getter
public class UserLoginResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String jwtToken;
    private final String refreshToken;

    public UserLoginResponse(User user, String jwtToken, String refreshToken) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
    }
}
