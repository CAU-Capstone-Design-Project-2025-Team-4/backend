package com.capstone2025.team4.backend.service.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class TemplateLongDTO {
    private Long id;
    private String name;
    private String creator;
    private byte[] thumbnail;
    private Map<Integer, byte[]> slideThumbnails;

    @QueryProjection
    public TemplateLongDTO(Long id, String name, String creator, byte[] thumbnail, Map<Integer, byte[]> slideThumbnails) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.thumbnail = thumbnail;
        this.slideThumbnails = slideThumbnails;
    }
}
