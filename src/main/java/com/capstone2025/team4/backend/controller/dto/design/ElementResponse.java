package com.capstone2025.team4.backend.controller.dto.design;

import com.capstone2025.team4.backend.domain.design.SlideElement;
import lombok.Getter;

@Getter
public class ElementResponse {
    private Long id;
    private String url;
    private Long x;
    private Long y;
    private Long width;
    private Long height;
    private Double angle;

    public ElementResponse(SlideElement slideElement) {
        this.id = slideElement.getId();
        this.url = slideElement.getElement().getUrl();
        this.x = slideElement.getX();
        this.y = slideElement.getY();
        this.width = slideElement.getWidth();
        this.height = slideElement.getHeight();
        this.angle = slideElement.getAngle();
    }
}
