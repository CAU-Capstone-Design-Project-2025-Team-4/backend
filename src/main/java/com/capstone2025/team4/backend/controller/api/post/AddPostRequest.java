package com.capstone2025.team4.backend.controller.api.post;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddPostRequest {
    @NotNull(message = "유저 정보는 필수입니다")
    private final Long userId;

    @NotNull(message = "디자인 정보는 필수입니다")
    private final Long designId;

    @NotNull(message = "제목은 필수입니다")
    private final String title;

    @NotNull(message = "내용은 필수입니다")
    private final String content;
}
