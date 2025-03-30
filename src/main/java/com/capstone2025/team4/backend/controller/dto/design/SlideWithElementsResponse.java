package com.capstone2025.team4.backend.controller.dto.design;

import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.design.SlideElement;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SlideWithElementsResponse {

    private Long id;
    private Integer order;
    private List<ElementResponse> slideElements = new ArrayList<>();

    public SlideWithElementsResponse(Slide slide) {
        this.id = slide.getId();
        this.order = slide.getOrder();
        for (SlideElement slideElement : slide.getSlideElementList()) {
            slideElements.add(new ElementResponse(slideElement));
        }
    }
}
