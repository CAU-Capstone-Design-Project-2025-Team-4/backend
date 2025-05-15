package com.capstone2025.team4.backend.domain.element.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Vector3 {
    private int x;
    private int y;
    private int z;

    @Builder
    public Vector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 copy() {
        Vector3 copy = new Vector3();
        copy.x = this.x;
        copy.y = this.y;
        copy.z = this.z;
        return copy;
    }
}