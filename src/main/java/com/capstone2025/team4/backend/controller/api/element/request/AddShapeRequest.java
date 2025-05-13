package com.capstone2025.team4.backend.controller.api.element.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddShapeRequest extends ElementRequest {
    @NotBlank(message = "path가 비어있습니다")
    @NotNull(message = "path는 필수입니다")
    private String path;

    @NotNull(message = "색 정보는 필수입니다")
    @NotBlank(message = "색 정보가 비어있습니다")
    private String color;
}
