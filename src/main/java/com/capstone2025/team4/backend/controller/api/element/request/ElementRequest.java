package com.capstone2025.team4.backend.controller.api.element.request;

import com.capstone2025.team4.backend.domain.element.border.BorderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public abstract class ElementRequest {
    @NotNull(message = "유저 정보는 필수입니다")
    private Long userId;

    @NotNull(message = "슬라이드 정보는 필수입니다")
    private Long slideId;

    @NotNull(message = "테두리 타입 정보는 필수입니다")
    private BorderType borderType;

    @NotNull(message = "테두리 색 정보는 필수입니다")
    @NotBlank(message = "테두리 색 정보가 비어있습니다")
    private String borderColor;

    @NotNull(message = "테두리 두께 정보는 필수입니다")
    private Long borderThickness;

    @NotNull(message = "x 좌표는 필수입니다")
    private Long x;

    @NotNull(message = "y 좌표는 필수입니다")
    private Long y;

    @NotNull(message = "z 좌표는 필수입니다")
    private Long z;

    @NotNull(message = "각도는 필수입니다")
    private Double angle;

    @NotNull(message = "width는 필수입니다")
    private Long width;

    @NotNull(message = "height는 필수입니다")
    private Long height;
}
