package com.capstone2025.team4.backend.service.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostSimpleDTO {

    private Long id;

    private String userEmail;

    private String title;

    private LocalDateTime createdAt;

    @QueryProjection
    public PostSimpleDTO(Long id, String userEmail, String title, LocalDateTime createdAt) {
        this.id = id;
        this.userEmail = userEmail;
        this.title = title;
        this.createdAt = createdAt;
    }
}
