package com.capstone2025.team4.backend.domain.design;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Slide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slide_order")
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "design_id")
    private Design design;

    @OneToMany(mappedBy = "slide", fetch = FetchType.LAZY)
    private List<SlideElement> slideElementList = new ArrayList<>();

    @Builder
    private Slide(Integer order, Design design, List<SlideElement> slideElementList) {
        this.order = order;
        this.design = design;
        this.slideElementList = slideElementList;
    }

}
