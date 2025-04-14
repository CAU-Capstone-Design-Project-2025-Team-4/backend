package com.capstone2025.team4.backend.controller.dto.element.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateShapeRequest {
    @NotNull(message = "사용자 정보는 필수입니다")
    Long userId;
    @NotNull(message = "요소 정보는 필수입니다")
    Long elementId;
    @NotNull(message = "경로(path) 정보는 필수입니다")
    @NotBlank(message = "경로(path) 정보가 비어 있습니다")
    String path;
    @NotNull(message = "색 정보는 필수입니다")
    @NotBlank(message = "색 정보가 비어 있습니다")
    String color;
}
