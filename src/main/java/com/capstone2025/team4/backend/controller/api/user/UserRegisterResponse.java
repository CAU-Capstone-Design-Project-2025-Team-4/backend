package com.capstone2025.team4.backend.controller.api.user;

import com.capstone2025.team4.backend.domain.User;
import lombok.Getter;

@Getter
public class UserRegisterResponse {
    private final Long id;
    private final String name;
    private final String email;

    public UserRegisterResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
