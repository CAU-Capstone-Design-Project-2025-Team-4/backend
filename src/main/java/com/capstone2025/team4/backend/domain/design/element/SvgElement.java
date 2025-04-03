package com.capstone2025.team4.backend.domain.design.element;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("SVG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class SvgElement extends Element{

    @Lob
    private String content;

}