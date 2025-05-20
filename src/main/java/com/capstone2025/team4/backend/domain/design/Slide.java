package com.capstone2025.team4.backend.domain.design;

import com.capstone2025.team4.backend.domain.element.Element;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "SLIDE_DESIGN_ORDER_UNIQUE", columnNames = {"design_id", "slide_order"})})
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

    @OneToMany(mappedBy = "slide", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Element> slideElementList = new ArrayList<>();

    @Lob
    private byte[] thumbnail;

    @Builder
    private Slide(Integer order, Design design, List<Element> slideElementList, byte[] thumbnail) {
        this.order = order;
        this.design = design;
        this.slideElementList = slideElementList;
        this.thumbnail = thumbnail;
    }

    public void changeThumbnail(byte[] image) {
        this.thumbnail = image;
    }
}
