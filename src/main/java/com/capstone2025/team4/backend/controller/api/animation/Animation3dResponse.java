package com.capstone2025.team4.backend.controller.api.animation;

import com.capstone2025.team4.backend.domain.animation3d.Animation3D;
import com.capstone2025.team4.backend.domain.animation3d.Animation3dType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Animation3dResponse {

    private Long id;

    private Long elementId;

    private Animation3dType type;

    public Animation3dResponse(Animation3D animation3D) {
        this.id = animation3D.getId();
        this.elementId = animation3D.getSpatial().getId();
        this.type = animation3D.getType();
    }
}
