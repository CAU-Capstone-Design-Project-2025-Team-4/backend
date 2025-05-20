package com.capstone2025.team4.backend.controller.api.post;

import com.capstone2025.team4.backend.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostResponse {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Long designId;
    private LocalDateTime createdAt;


    public PostResponse(Post post) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.designId = post.getDesign().getId();
        this.createdAt = post.getCreatedAt();
    }
}
