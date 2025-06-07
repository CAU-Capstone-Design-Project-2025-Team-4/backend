package com.capstone2025.team4.backend.controller.api.frame;

import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import com.capstone2025.team4.backend.domain.element.spatial.Frame;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FrameResponse {
    private Long frameId;

    private Long spatialId;

    private String name;

    private CameraTransform cameraTransform;

    public FrameResponse(Frame frame) {
        this.frameId = frame.getId();
        this.spatialId = frame.getSpatial().getId();
        this.name = frame.getName();
        this.cameraTransform = frame.getCameraTransform();
    }
}
