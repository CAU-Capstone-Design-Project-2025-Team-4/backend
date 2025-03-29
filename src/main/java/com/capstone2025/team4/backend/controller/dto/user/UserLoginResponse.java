package com.capstone2025.team4.backend.controller.dto.user;

import com.capstone2025.team4.backend.domain.User;
import lombok.Getter;

@Getter
public class UserLoginResponse {
    private Long id;
    private String name;
    private String email;
    private String jwtToken;

    public UserLoginResponse(User user, String jwtToken) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.jwtToken = jwtToken;
    }
}
