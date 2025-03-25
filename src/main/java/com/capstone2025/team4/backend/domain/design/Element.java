package com.capstone2025.team4.backend.domain.design;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Element {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private Type type;

    private Boolean isDefault;

    private Float x;

    private Float y;

    private Long width;

    private Long height;

    private Double angle;

    @Embedded
    private Color color;

    @Builder
    private Element(Type type, String url, Boolean isDefault, Float x, Float y, Long width, Long height, Double angle, Color color) {
        this.type = type;
        this.url = url;
        this.isDefault = isDefault;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.color = color;
    }
}
