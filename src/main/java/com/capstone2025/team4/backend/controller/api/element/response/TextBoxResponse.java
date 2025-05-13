package com.capstone2025.team4.backend.controller.api.element.response;

import com.capstone2025.team4.backend.controller.api.element.ElementType;
import com.capstone2025.team4.backend.domain.element.TextAlign;
import com.capstone2025.team4.backend.domain.element.TextBox;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextBoxResponse extends ElementResponse {
    private String text;
    private Long size;
    private Long weight;
    private String fontFamily;
    private TextAlign textAlign;
    private final ElementType type = ElementType.TEXT_BOX;

    public static TextBoxResponse createFrom(TextBox textBox) {
        TextBoxResponse textBoxDTO = new TextBoxResponse();
        textBoxDTO.text = textBox.getText();
        textBoxDTO.size = textBox.getSize();
        textBoxDTO.weight = textBox.getWeight();
        textBoxDTO.fontFamily = textBox.getFontFamily();
        textBoxDTO.textAlign = textBox.getAlign();
        return textBoxDTO;
    }
}
