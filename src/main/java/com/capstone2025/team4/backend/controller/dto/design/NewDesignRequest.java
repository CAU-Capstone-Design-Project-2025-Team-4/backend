package com.capstone2025.team4.backend.controller.dto.design;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NewDesignRequest {
    @NotNull(message = "유저 정보는 필수입니다")
    private Long userId;

    private Long sourceId;

    @NotNull(message = "공유인지 아닌지 선택해주세요")
    private Boolean isShared;
}
