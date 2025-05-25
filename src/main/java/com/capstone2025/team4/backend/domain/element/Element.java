package com.capstone2025.team4.backend.domain.element;

import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.element.border.BorderRef;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Getter
@SuperBuilder
public class Element {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slide_id")
    private Slide slide;

    @Embedded
    private BorderRef borderRef;

    private Long x;

    private Long y;

    private Long z;

    private Long width;

    private Long height;

    private Double angle;

    public final Element copy(Slide destSlide) {
        Element copy = createNewInstance();
        copy.slide = destSlide;
        copy.addToSlide(destSlide);
        if (this.getBorderRef() == null) {
            copy.borderRef = null;
        } else {
            copy.borderRef = this.getBorderRef().copy();
        }
        copy.x = this.x;
        copy.y = this.y;
        copy.z = this.z;
        copy.width = this.width;
        copy.height = this.height;
        copy.angle = this.angle;
        copyElementFields(copy);
        return copy;
    }

    @PrePersist
    public void onPrePersist() {
        if (slide != null && slide.getDesign() != null) {
            slide.getDesign().preUpdate();
        }
    }

    @PreUpdate
    public void onPreUpdate() {
        if (slide != null && slide.getDesign() != null) {
            slide.getDesign().preUpdate();
        }
    }

    @PostRemove
    public void onPostRemove() {
        if (slide != null && slide.getDesign() != null) {
            slide.getDesign().preUpdate();
        }
    }

    protected Element createNewInstance() {throw new RuntimeException();}

    protected void copyElementFields(Element copy) {
        throw new RuntimeException();
    }
    public void update(BorderRef borderRef, long x, long y, long z, long width, long height, double angle) {
        this.borderRef = borderRef;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.angle = angle;
    }

    public void addToSlide(Slide slide) {
        slide.getSlideElementList().add(this);
        this.slide = slide;
    }
}
