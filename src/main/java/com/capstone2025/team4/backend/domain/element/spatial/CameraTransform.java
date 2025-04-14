package com.capstone2025.team4.backend.domain.element.spatial;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CameraTransform {
    private Long positionX;

    private Long positionY;

    private Long positionZ;

    private Long rotationX;

    private Long rotationY;

    private Long rotationZ;

    @Builder
    public CameraTransform(Long positionX, Long positionY, Long positionZ, Long rotationX, Long rotationY, Long rotationZ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
    }

    public CameraTransform copy() {
        CameraTransform cameraTransform = new CameraTransform();
        cameraTransform.positionX = this.positionX;
        cameraTransform.positionY = this.positionY;
        cameraTransform.positionZ = this.positionZ;
        cameraTransform.rotationX = this.rotationX;
        cameraTransform.rotationY = this.rotationY;
        cameraTransform.rotationZ = this.rotationZ;
        return cameraTransform;
    }
}
