package com.capstone2025.team4.backend.controller.dto.user;

import com.capstone2025.team4.backend.domain.User;
import lombok.Getter;

@Getter
public class UserRegisterResponse {
    private Long id;
    private String name;
    private String email;

    public UserRegisterResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
