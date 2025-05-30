package com.capstone2025.team4.backend.service.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostFullDTO {
    private Long id;

    private Long userId;

    private String userEmail;

    private String username;

    private Long designId;

    private LocalDateTime createdAt;

    private String title;

    private String content;

    @QueryProjection
    public PostFullDTO(Long id, Long userId, String userEmail, String username, Long designId, LocalDateTime createdAt, String title, String content) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.username = username;
        this.designId = designId;
        this.createdAt = createdAt;
        this.title = title;
        this.content = content;
    }
}
