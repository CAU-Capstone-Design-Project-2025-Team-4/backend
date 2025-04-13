package com.capstone2025.team4.backend.domain.element.border;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class BorderRef {

    @Enumerated(value = EnumType.STRING)
    private BorderType type;

    private String color;

    private Long thickness;
}
