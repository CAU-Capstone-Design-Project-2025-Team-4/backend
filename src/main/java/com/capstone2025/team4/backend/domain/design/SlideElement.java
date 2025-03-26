package com.capstone2025.team4.backend.domain.design;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SlideElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "slide_id")
    private Slide slide;

    @ManyToOne
    @JoinColumn(name = "element_id")
    private Element element;

    private Long x;

    private Long y;

    private Long width;

    private Long height;

    private Double angle;

    @Builder
    private SlideElement(Slide slide, Element element, Long x, Long y, Long width, Long height, Double angle) {
        this.slide = slide;
        this.element = element;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
    }

    public SlideElement update(Long x, Long y, Long width, Long height, Double angle) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        return this;
    }
}
