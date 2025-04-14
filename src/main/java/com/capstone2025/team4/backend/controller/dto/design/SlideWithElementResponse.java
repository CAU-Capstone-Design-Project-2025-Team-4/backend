package com.capstone2025.team4.backend.controller.dto.design;

import com.capstone2025.team4.backend.controller.dto.element.response.ElementResponse;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.element.Element;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SlideWithElementResponse {

    private final Long id;
    private final Integer order;
    private final List<ElementResponse> slideElements = new ArrayList<>();

    public SlideWithElementResponse(Slide slide) {
        this.id = slide.getId();
        this.order = slide.getOrder();
        if (slide.getSlideElementList() != null) {
            for (Element slideElement : slide.getSlideElementList()) {
                slideElements.add(ElementResponse.create(slideElement));
//                slideElements.add(new ElementResponse(slideElement));
            }
        }
    }
}
