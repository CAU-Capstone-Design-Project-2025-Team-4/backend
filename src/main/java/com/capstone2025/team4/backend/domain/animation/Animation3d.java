package com.capstone2025.team4.backend.domain.animation;

import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ANIMATION_3D")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class Animation3d extends Animation{

    @Embedded
    CameraTransform cameraTransform;

    @Override
    protected Animation createNewInstance() {
        return new Animation3d();
    }

    @Override
    protected void copySpecific(Animation destAnimation) {
        ((Animation3d) destAnimation).cameraTransform = this.cameraTransform;
    }
}
