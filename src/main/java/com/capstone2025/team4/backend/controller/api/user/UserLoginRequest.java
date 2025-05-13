package com.capstone2025.team4.backend.controller.api.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserLoginRequest {

    @Email(message = "이메일 형식을 확인해주세요")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    private String jwtToken;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
