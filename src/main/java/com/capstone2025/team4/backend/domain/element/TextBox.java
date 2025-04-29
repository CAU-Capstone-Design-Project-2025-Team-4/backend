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

    private String fontFamily;

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
        textBoxCopy.fontFamily = this.fontFamily;
    }

    public void update(String text, Long size, Long weight, String fontFamily, TextAlign align) {
        this.text = text;
        this.size = size;
        this.weight = weight;
        this.fontFamily = fontFamily;
        this.align = align;
    }
}
