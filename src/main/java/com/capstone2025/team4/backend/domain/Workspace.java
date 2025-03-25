package com.capstone2025.team4.backend.domain;

import com.capstone2025.team4.backend.domain.design.Design;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workspace", fetch = FetchType.LAZY)
    private List<Design> designList = new ArrayList<>();

    public Workspace(User user) {
        this.user = user;
    }
}
