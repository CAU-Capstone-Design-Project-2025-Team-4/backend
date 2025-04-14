package com.capstone2025.team4.backend.controller.dto.element.request;

import com.capstone2025.team4.backend.domain.element.spatial.CameraMode;
import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateSpatialRequest {
    @NotNull(message = "사용자 정보는 필수입니다")
    Long userId;
    @NotNull(message = "요소 정보는 필수입니다")
    Long elementId;
    @NotNull(message = "카메라 모드 정보는 필수입니다")
    CameraMode cameraMode;
    @NotNull(message = "카메라 위치 및 회정 정보는 필수입니다")
    CameraTransform cameraTransform;
    @NotNull(message = "content는 필수입니다")
    @NotBlank(message = "content가 비어있습니다")
    String content;
    @NotNull(message = "배경색은 필수입니다")
    @NotBlank(message = "배경색은 비어있습니다")
    String backgroundColor;
}
