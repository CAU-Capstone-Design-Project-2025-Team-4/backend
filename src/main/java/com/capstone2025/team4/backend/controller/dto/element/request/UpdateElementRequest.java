package com.capstone2025.team4.backend.controller.dto.element.request;

import com.capstone2025.team4.backend.domain.element.border.BorderRef;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateElementRequest{

    @NotNull(message = "유저 정보는 필수입니다")
    private Long userId;

    @NotNull(message = "요소 정보는 필수입니다")
    private Long elementId;

    @NotNull(message = "테두리 정보는 필수입니다")
    private BorderRef borderRef;

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
