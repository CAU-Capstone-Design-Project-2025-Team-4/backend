package com.capstone2025.team4.backend.controller.dto.design;

import com.capstone2025.team4.backend.domain.design.Design;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class DesignWithoutSlidesResponse {
    private Long id;
    private Boolean shared;
    private LocalDateTime createdAt;

    public DesignWithoutSlidesResponse(Design design) {
        this.id = design.getId();
        this.shared = design.getShared();
        this.createdAt = design.getCreatedAt();
    }
}
