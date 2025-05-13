package com.capstone2025.team4.backend.controller.api.design;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NewSlideRequest {
    @NotNull(message = "유저 정보는 필수입니다")
    private Long userId;
    @NotNull(message = "디자인 정보는 필수입니다")
    private Long designId;
    @NotNull(message = "순서 정보는 필수입니다")
    private Integer order;

}
