package com.capstone2025.team4.backend.domain.element;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("TEXT_BOX")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class TextBox extends Element {

    @Lob
    private String text;

    private Long size;

    private Long weight;

    @Enumerated(value = EnumType.STRING)
    private TextAlign align;

    @Override
    protected Element createNewInstance() {
        return new TextBox();
    }

    @Override
    protected void copyElementFields(Element copy) {
        TextBox textBoxCopy = (TextBox) copy;
        textBoxCopy.text = this.text;
        textBoxCopy.size = this.size;
        textBoxCopy.weight = this.weight;
    }
}
