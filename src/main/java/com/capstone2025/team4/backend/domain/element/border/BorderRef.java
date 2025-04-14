package com.capstone2025.team4.backend.domain.element.border;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class BorderRef {

    @Enumerated(value = EnumType.STRING)
    private BorderType borderType;

    private String color;

    private Long thickness;

    @Builder
    public BorderRef(BorderType borderType, String color, Long thickness) {
        this.borderType = borderType;
        this.color = color;
        this.thickness = thickness;
    }

    public BorderRef copy() {
        BorderRef borderRef = new BorderRef();
        borderRef.borderType = this.borderType;
        borderRef.color = this.color;
        borderRef.thickness = this.thickness;
        return borderRef;
    }
}
