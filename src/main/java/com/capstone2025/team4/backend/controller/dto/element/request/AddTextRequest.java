package com.capstone2025.team4.backend.controller.dto.element.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddTextRequest extends ElementRequest {
    @NotNull(message = "Text는 필수입니다")
    @NotBlank(message = "Text가 비어 있습니다")
    private String text;

    @NotNull(message = "텍스트 크기는 필수입니다")
    private Long size;

    @NotNull(message = "굵기는 필수입니다")
    private Long weight;
}
