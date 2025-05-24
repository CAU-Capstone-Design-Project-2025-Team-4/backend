package com.capstone2025.team4.backend.service.dto;

import com.capstone2025.team4.backend.domain.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostFullDTO {
    private Long id;

    private String userEmail;

    private Long designId;

    private LocalDateTime createdAt;

    private String title;

    private String content;

    @QueryProjection
    public PostFullDTO(Long id, String userEmail, Long designId, LocalDateTime createdAt, String title, String content) {
        this.id = id;
        this.userEmail = userEmail;
        this.designId = designId;
        this.createdAt = createdAt;
        this.title = title;
        this.content = content;
    }
}
