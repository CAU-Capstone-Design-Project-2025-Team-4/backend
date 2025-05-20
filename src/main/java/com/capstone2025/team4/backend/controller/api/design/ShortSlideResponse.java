package com.capstone2025.team4.backend.controller.api.design;

import com.capstone2025.team4.backend.domain.design.Slide;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ShortSlideResponse {
    private Long id;

    private byte[] thumbnail;

    public ShortSlideResponse(Slide slide) {
        this.id = slide.getId();
        this.thumbnail = slide.getThumbnail();
    }
}
