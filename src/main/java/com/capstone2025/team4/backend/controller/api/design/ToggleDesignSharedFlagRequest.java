package com.capstone2025.team4.backend.controller.api.design;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ToggleDesignSharedFlagRequest {

    @NotNull(message = "유저 정보는 필수입니다")
    private final Long userId;

    @NotNull(message = "디자인 정보는 필수입니다")
    private final Long designId;

    @NotNull(message = "flag 정보는 필수입니다")
    private final Boolean flag;
}
