package com.capstone2025.team4.backend.domain.element.spatial;

import jakarta.persistence.Embeddable;

@Embeddable
public class CameraTransform {
    private Long position_x;

    private Long position_y;

    private Long position_z;

    private Long rotation_x;

    private Long rotation_y;

    private Long rotation_z;
}
