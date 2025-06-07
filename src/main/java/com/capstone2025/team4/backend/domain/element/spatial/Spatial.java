package com.capstone2025.team4.backend.domain.element.spatial;

import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.domain.element.model.Model;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "spatial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Frame> frameList = new ArrayList<>();

    @Override
    protected Element createNewInstance() {
        return new Spatial();
    }

    @Override
    protected void copyElementFields(Element copy) {
        Spatial spatialCopy = (Spatial) copy;
        spatialCopy.cameraMode = this.cameraMode;
        spatialCopy.cameraTransform = this.cameraTransform.copy();
        spatialCopy.backgroundColor = this.backgroundColor;
        for (Model model : models) {
            spatialCopy.models.add(model.copy());
        }
        for (Frame frame : frameList) {
            spatialCopy.frameList.add(frame.copy(spatialCopy));
        }
    }

    public void update(CameraMode cameraMode, CameraTransform cameraTransform, String backgroundColor) {
        this.cameraMode = cameraMode;
        this.cameraTransform = cameraTransform;
        this.backgroundColor = backgroundColor;
        updateDesign();
    }

    public void addModel(Model model) {
        this.models.add(model);
        model.setSpatial(this);
        updateDesign();
    }

    public void addFrame(Frame frame) {
        this.frameList.add(frame);
        frame.setSpatial(this);
        updateDesign();
    }

    private void updateDesign() {
        this.getSlide().getDesign().preUpdate();
    }
}
