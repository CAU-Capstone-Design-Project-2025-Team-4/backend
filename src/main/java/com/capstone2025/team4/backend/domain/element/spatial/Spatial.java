package com.capstone2025.team4.backend.domain.element.spatial;

import com.capstone2025.team4.backend.domain.element.Element;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("SPATIAL_ELEMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
@Table(name = "spatial_element")
public class Spatial extends Element {

    @Enumerated(value = EnumType.STRING)
    private CameraMode cameraMode;

    @Embedded
    private CameraTransform cameraTransform;

    private String content;

    private String backgroundColor;

    @Override
    protected Element createNewInstance() {
        return new Spatial();
    }

    @Override
    protected void copyElementFields(Element copy) {
        Spatial spatialCopy = (Spatial) copy;
        spatialCopy.cameraMode = this.cameraMode;
        spatialCopy.cameraTransform = this.cameraTransform.copy();
        spatialCopy.content = this.content;
        spatialCopy.backgroundColor = this.backgroundColor;
    }

    public void update(CameraMode cameraMode, CameraTransform cameraTransform, String content, String backgroundColor) {
        this.cameraMode = cameraMode;
        this.cameraTransform = cameraTransform;
        this.content = content;
        this.backgroundColor = backgroundColor;
    }
}
