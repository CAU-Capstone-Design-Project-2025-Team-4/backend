package com.capstone2025.team4.backend.domain.animation3d;

import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Animation3D {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spatial_id")
    private Spatial spatial;

    @Enumerated(EnumType.STRING)
    private Animation3dType type;

    @Builder
    public Animation3D(Spatial spatial, Animation3dType type) {
        this.spatial = spatial;
        this.type = type;
    }

    public Animation3D copy(Spatial spatial) {
        Animation3D copy = new Animation3D();
        copy.spatial = spatial;
        copy.type = this.type;
        return copy;
    }
}
