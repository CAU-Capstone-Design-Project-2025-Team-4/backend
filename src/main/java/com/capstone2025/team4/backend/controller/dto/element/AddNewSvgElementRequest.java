package com.capstone2025.team4.backend.controller.dto.element;

import com.capstone2025.team4.backend.domain.design.Type;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class AddNewSvgElementRequest {

    @NotNull(message = "유저 정보는 필수입니다")
    private Long userId;

    @NotNull(message = "슬라이드 정보는 필수입니다")
    private Long slideId;

    @NotNull(message = "Svg 내용은 필수입니다")
    private String svgContent;

    @NotNull(message = "요소 타입은 필수입니다")
    private Type type;

    @NotNull(message = "x 좌표는 필수입니다")
    private Long x;

    @NotNull(message = "y 좌표는 필수입니다")
    private Long y;

    @NotNull(message = "각도는 필수입니다")
    private Double angle;

    @NotNull(message = "width는 필수입니다")
    private Long width;

    @NotNull(message = "height는 필수입니다")
    private Long height;
}
