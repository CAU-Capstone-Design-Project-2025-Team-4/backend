package com.capstone2025.team4.backend.domain.element.spatial;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spatial_element_id")
    private Spatial spatial;

    private String url;

    @Builder
    public Model(String url) {
        this.url = url;
    }

    public Model copy() {
        Model copy = new Model();
        copy.url = this.url;
        return copy;
    }

}
