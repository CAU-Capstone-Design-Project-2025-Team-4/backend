package com.capstone2025.team4.backend.service.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TemplateShortDTO {

    private Long id;
    private String name;
    private byte[] thumbnail;

    @QueryProjection
    public TemplateShortDTO(Long id, String name, byte[] thumbnail) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
    }
}
