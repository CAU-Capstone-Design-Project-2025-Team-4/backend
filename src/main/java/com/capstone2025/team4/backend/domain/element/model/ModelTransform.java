package com.capstone2025.team4.backend.domain.element.model;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class ModelTransform {

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "x", column = @Column(name = "position_x")),
        @AttributeOverride(name = "y", column = @Column(name = "position_y")),
        @AttributeOverride(name = "z", column = @Column(name = "position_z"))
    })
    private Vector3 position;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "rotation_x")),
            @AttributeOverride(name = "y", column = @Column(name = "rotation_y")),
            @AttributeOverride(name = "z", column = @Column(name = "rotation_z"))
    })
    private Vector3 rotation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "scale_x")),
            @AttributeOverride(name = "y", column = @Column(name = "scale_y")),
            @AttributeOverride(name = "z", column = @Column(name = "scale_z"))
    })
    private Vector3 scale;

    @Builder
    public ModelTransform(Vector3 position, Vector3 rotation, Vector3 scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }
}
