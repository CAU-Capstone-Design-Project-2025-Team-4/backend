package com.capstone2025.team4.backend.controller.api.design;

import com.capstone2025.team4.backend.domain.design.Design;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class DesignShortResponse {
    private Long id;
    private Boolean shared;
    private LocalDateTime createdAt;
    private String name;
    private LocalDateTime updatedAt;

    public DesignShortResponse(Design design) {
        this.id = design.getId();
        this.shared = design.getShared();
        this.createdAt = design.getCreatedAt();
        this.name = design.getName();
        this.updatedAt = design.getUpdatedAt();
    }
}
