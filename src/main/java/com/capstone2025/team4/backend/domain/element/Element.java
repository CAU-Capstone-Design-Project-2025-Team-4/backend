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
}
