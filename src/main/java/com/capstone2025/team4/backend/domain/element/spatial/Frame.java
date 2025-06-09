package com.capstone2025.team4.backend.domain.element.spatial;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Frame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spatial_element_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Setter
    private Spatial spatial;

    @Embedded
    private CameraTransform cameraTransform;

    @Builder
    public Frame(String name, Spatial spatial, CameraTransform cameraTransform) {
        this.name = name;
        this.spatial = spatial;
        this.cameraTransform = cameraTransform;
    }

    public void update(CameraTransform cameraTransform, String name) {
        this.cameraTransform = cameraTransform;
        this.name = name;
    }

    public Frame copy(Spatial copyDest) {
        Frame copy = new Frame();
        copy.name = this.name;
        copy.spatial = copyDest;
        copy.cameraTransform = cameraTransform.copy();
        return copy;
    }
}
