package com.capstone2025.team4.backend.domain.post;

import com.capstone2025.team4.backend.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id")
    private User designer;

    private String title;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @Builder
    private Post(User designer, String title, String description, Category category) {
        this.designer = designer;
        this.title = title;
        this.description = description;
        this.category = category;
    }
}
