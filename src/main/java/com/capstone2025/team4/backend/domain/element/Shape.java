package com.capstone2025.team4.backend.domain.element;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("SHAPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class Shape extends Element {

    @Lob
    private String path;

    private String color;

    @Override
    protected Element createNewInstance() {
        return new Shape();
    }

    @Override
    protected void copyElementFields(Element copy) {
        Shape shapeCopy = (Shape) copy;
        shapeCopy.path = this.path;
        shapeCopy.color = this.color;
    }

    public void update(String path, String color) {
        this.path = path;
        this.color = color;
        this.getSlide().getDesign().preUpdate();
    }
}
