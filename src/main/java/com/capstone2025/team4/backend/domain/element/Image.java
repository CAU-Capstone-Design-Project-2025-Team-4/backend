package com.capstone2025.team4.backend.domain.element;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("IMAGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class Image extends Element {

    private String url;

    @Override
    protected Element createNewInstance() {
        return new Image();
    }

    @Override
    protected void copyElementFields(Element copy) {
        ((Image) copy).url = this.url;
    }
}
