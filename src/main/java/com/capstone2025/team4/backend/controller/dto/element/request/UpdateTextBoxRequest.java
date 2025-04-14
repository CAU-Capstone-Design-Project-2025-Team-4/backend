package com.capstone2025.team4.backend.controller.dto.element.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateTextBoxRequest {
    @NotNull(message = "사용자 정보는 필수입니다")
    Long userId;
    @NotNull(message = "요소 정보는 필수입니다")
    Long elementId;
    @NotNull(message = "텍스트 정보는 필수입니다")
    String text;
    @NotNull(message = "크기 정보는 필수입니다")
    Long size;
    @NotNull(message = "굵기 정보는 필수입니다")
    Long weight;
}
