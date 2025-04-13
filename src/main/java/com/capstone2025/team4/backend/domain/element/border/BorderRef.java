package com.capstone2025.team4.backend.domain.element.border;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BorderRef {

    @Enumerated(value = EnumType.STRING)
    private BorderType type;

    private String color;

    private Long thickness;

    @Builder
    public BorderRef(BorderType borderType, String color, Long thickness) {
        this.type = borderType;
        this.color = color;
        this.thickness = thickness;
    }

    public BorderRef copy() {
        BorderRef borderRef = new BorderRef();
        borderRef.type = this.type;
        borderRef.color = this.color;
        borderRef.thickness = this.thickness;
        return borderRef;
    }
}
