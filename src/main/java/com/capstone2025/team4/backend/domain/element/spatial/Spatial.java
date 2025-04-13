package com.capstone2025.team4.backend.domain.element.spatial;

import com.capstone2025.team4.backend.domain.File;
import com.capstone2025.team4.backend.domain.element.Element;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("TEXT_BOX")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class Spatial extends Element {

    @Enumerated(value = EnumType.STRING)
    private CameraMode cameraMode;

    @Embedded
    private CameraTransform cameraTransform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

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
        spatialCopy.file = this.file;
        spatialCopy.backgroundColor = this.backgroundColor;
    }
}
