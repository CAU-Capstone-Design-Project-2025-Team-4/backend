package com.capstone2025.team4.backend.domain.element.model;

import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spatial_element_id")
    private Spatial spatial;

    private String url;

    @Enumerated(EnumType.STRING)
    private ModelShader shader;

    @Embedded
    private ModelTransform modelTransform;

    @Builder
    public Model(String name, String url, ModelShader shader, ModelTransform modelTransform) {
        this.name = name;
        this.url = url;
        this.shader = shader;
        this.modelTransform = modelTransform;
    }

    public Model copy() {
        Model copy = new Model();
        copy.url = this.url;
        return copy;
    }

    public void update(String name, ModelShader shader, ModelTransform modelTransform) {
        this.name = name;
        this.shader = shader;
        this.modelTransform = modelTransform;
    }
}
