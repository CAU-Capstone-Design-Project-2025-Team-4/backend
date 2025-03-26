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

    @Column(nullable = false)
    private Boolean isDefault;

    private Long x;

    private Long y;

    private Long width;

    private Long height;

    private Double angle;

    @Builder
    private Element(Type type, String url, Boolean isDefault, Long x, Long y, Long width, Long height, Double angle) {
        this.type = type;
        this.url = url;
        this.isDefault = isDefault;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
    }
}
