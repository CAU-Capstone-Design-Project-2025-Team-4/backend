package com.capstone2025.team4.backend.controller.dto.design;

import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DesignWithSlidesFileElementsResponse {
    private Long id;
    private Boolean shared;
    private List<SlideResponseWithFileElement> slideList = new ArrayList<>();

    public DesignWithSlidesFileElementsResponse(Design design) {
        this.id = design.getId();
        this.shared = design.getShared();
        if (design.getSlideList() != null) {
            for (Slide slide : design.getSlideList()) {
                slideList.add(new SlideResponseWithFileElement(slide));
            }
        }
    }
}
