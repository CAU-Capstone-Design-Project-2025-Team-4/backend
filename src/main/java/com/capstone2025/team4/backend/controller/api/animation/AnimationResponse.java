package com.capstone2025.team4.backend.controller.api.animation;

import com.capstone2025.team4.backend.domain.animation.Animation;
import com.capstone2025.team4.backend.domain.animation.Animation3d;
import com.capstone2025.team4.backend.domain.animation.AnimationTiming;
import com.capstone2025.team4.backend.domain.animation.AnimationType;
import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AnimationResponse {

    private Long id;

    private Long elementId;

    private AnimationType type;

    private Integer duration;

    private AnimationTiming timing;

    private CameraTransform cameraTransform = null;

    public AnimationResponse(Animation animation) {
        this.id = animation.getId();
        this.elementId = animation.getElement().getId();
        this.type = animation.getAnimationType();
        this.duration = animation.getDuration();
        this.timing = animation.getTiming();
        if (animation instanceof Animation3d animation3d) {
            cameraTransform = animation3d.getCameraTransform();
        }
    }
}