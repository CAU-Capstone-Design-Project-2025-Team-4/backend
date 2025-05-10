package com.capstone2025.team4.backend.controller.dto.element.response;

import com.capstone2025.team4.backend.controller.dto.element.ElementType;
import com.capstone2025.team4.backend.domain.element.spatial.CameraMode;
import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SpatialResponse extends ElementResponse {
    private CameraMode cameraMode;
    private CameraTransform cameraTransform;
    private String content;
    private String backgroundColor;
    private final ElementType type = ElementType.SPATIAL;

    public static SpatialResponse createFrom(Spatial spatial) {
        SpatialResponse spatialDTO = new SpatialResponse();
        spatialDTO.cameraMode = spatial.getCameraMode();
        spatialDTO.cameraTransform = spatial.getCameraTransform();
        spatialDTO.content = spatial.getContent();
        spatialDTO.backgroundColor = spatial.getBackgroundColor();
        return spatialDTO;
    }
}
