package com.capstone2025.team4.backend.domain.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean shared;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Design source;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "design", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderColumn(name = "slide_order")
    private List<Slide> slideList = new ArrayList<>();

    @Lob
    private byte[] thumbnail;

    private Boolean inPost;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Builder
    private Design(String name, Workspace workspace, User user, Boolean shared, Design source, List<Slide> slideList) {
        this.name = name;
        this.workspace = workspace;
        this.user = user;
        this.shared = shared;
        this.source = source;
        if (slideList != null) {
            this.slideList = slideList;
        }
        this.inPost = false;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeThumbnail(byte[] image) {
        this.thumbnail = image;
    }

    public void changeInPost(boolean flag) {
        this.inPost = flag;
    }

    public void changeShared(boolean flag) {
        this.shared = flag;
    }
}
