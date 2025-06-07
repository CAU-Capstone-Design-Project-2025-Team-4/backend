package com.capstone2025.team4.backend.controller.api.frame;

import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateFrameRequest {

    @NotNull(message = "프레임 정보는 필수입니다")
    private Long frameId;

    @NotNull(message = "사용자 정보는 필수입니다")
    private Long userId;

    @NotNull(message = "spatial 정보는 필수입니다")
    private Long spatialId;

    @NotNull(message = "이름 정보는 필수입니다")
    private String name;

    @NotNull(message = "camera transform 정보는 필수입니다")
    private CameraTransform cameraTransform;
}
