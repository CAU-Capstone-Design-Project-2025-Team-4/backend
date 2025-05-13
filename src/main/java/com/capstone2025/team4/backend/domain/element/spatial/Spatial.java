package com.capstone2025.team4.backend.domain.element.spatial;

import com.capstone2025.team4.backend.domain.element.Element;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "spatial", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Model> models = new ArrayList<>();

    private String backgroundColor;

    @Override
    protected Element createNewInstance() {
        return new Spatial();
    }

    /**
     * spatial의 속성만 copy한다.
     * 모델 copy는 별도로 진행해야 된다
     * @param copy
     */
    @Override
    protected void copyElementFields(Element copy) {
        Spatial spatialCopy = (Spatial) copy;
        spatialCopy.cameraMode = this.cameraMode;
        spatialCopy.cameraTransform = this.cameraTransform.copy();
        spatialCopy.backgroundColor = this.backgroundColor;
    }

    public void update(CameraMode cameraMode, CameraTransform cameraTransform, String backgroundColor) {
        this.cameraMode = cameraMode;
        this.cameraTransform = cameraTransform;
        this.backgroundColor = backgroundColor;
    }

    public void addModel(Model model) {
        this.models.add(model);
        model.setSpatial(this);
    }
}
