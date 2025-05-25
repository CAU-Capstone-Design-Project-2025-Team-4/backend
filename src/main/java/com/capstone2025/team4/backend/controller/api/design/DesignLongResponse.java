package com.capstone2025.team4.backend.controller.api.design;

import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DesignLongResponse {
    private final Long id;
    private final Boolean shared;
    private final LocalDateTime createdAt;
    private final String name;
    private final LocalDateTime updatedAt;
    private final byte[] thumbnail;
    private final boolean inPost;
    private final List<SlideWithElementResponse> slideList = new ArrayList<>();

    public DesignLongResponse(Design design) {
        this.id = design.getId();
        this.shared = design.getShared();
        this.createdAt = design.getCreatedAt();
        this.name = design.getName();
        this.updatedAt = design.getUpdatedAt();
        this.thumbnail = design.getThumbnail();
        this.inPost = design.getInPost();
        if (design.getSlideList() != null) {
            for (Slide slide : design.getSlideList()) {
                slideList.add(new SlideWithElementResponse(slide));
            }
        }
    }
}
