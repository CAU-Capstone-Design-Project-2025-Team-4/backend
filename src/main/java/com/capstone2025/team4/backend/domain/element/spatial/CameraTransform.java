package com.capstone2025.team4.backend.domain.element.spatial;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CameraTransform {
    private Long position_x;

    private Long position_y;

    private Long position_z;

    private Long rotation_x;

    private Long rotation_y;

    private Long rotation_z;

    @Builder
    public CameraTransform(Long position_x, Long position_y, Long position_z, Long rotation_x, Long rotation_y, Long rotation_z) {
        this.position_x = position_x;
        this.position_y = position_y;
        this.position_z = position_z;
        this.rotation_x = rotation_x;
        this.rotation_y = rotation_y;
        this.rotation_z = rotation_z;
    }

    public CameraTransform copy() {
        CameraTransform cameraTransform = new CameraTransform();
        cameraTransform.position_x = this.position_x;
        cameraTransform.position_y = this.position_y;
        cameraTransform.position_z = this.position_z;
        cameraTransform.rotation_x = this.rotation_x;
        cameraTransform.rotation_y = this.rotation_y;
        cameraTransform.rotation_z = this.rotation_z;
        return cameraTransform;
    }
}
