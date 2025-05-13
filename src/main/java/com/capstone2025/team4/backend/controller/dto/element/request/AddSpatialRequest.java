package com.capstone2025.team4.backend.controller.dto.element.request;

import com.capstone2025.team4.backend.domain.element.spatial.CameraMode;
import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class AddSpatialRequest extends ElementRequest {
    @NotNull(message = "카메라 모드는 필수입니다")
    private CameraMode cameraMode;

    @NotNull(message = "카메라 위치 및 회정 정보는 필수입니다")
    private CameraTransform cameraTransform;

    private MultipartFile file;

    @NotNull(message = "배경색은 필수입니다")
    @NotBlank(message = "배경색 정보가 비어있습니다")
    private String backgroundColor;
}
