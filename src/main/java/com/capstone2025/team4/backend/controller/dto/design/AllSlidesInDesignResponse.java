package com.capstone2025.team4.backend.controller.dto.design;

import com.capstone2025.team4.backend.domain.design.Slide;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AllSlidesInDesignResponse {
    private Long size;
    private List<SlideWithElementResponse> slides = new ArrayList<>();

    public AllSlidesInDesignResponse(List<Slide> slideList) {
        for (Slide slide : slideList) {
            this.slides.add(new SlideWithElementResponse(slide));
        }
        this.size = (long) slideList.size();
    }
}
