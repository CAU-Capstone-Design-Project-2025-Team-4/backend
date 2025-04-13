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
public abstract class Element {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
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
        copy.borderRef = this.getBorderRef().copy();
        copy.x = this.x;
        copy.y = this.y;
        copy.z = this.z;
        copy.width = this.width;
        copy.height = this.height;
        copy.angle = this.angle;
        copyElementFields(copy);
        return copy;
    }

    protected abstract Element createNewInstance();
    protected abstract void copyElementFields(Element copy);
}
