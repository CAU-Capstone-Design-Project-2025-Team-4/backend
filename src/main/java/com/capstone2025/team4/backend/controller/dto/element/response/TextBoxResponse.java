package com.capstone2025.team4.backend.controller.dto.element.response;

import com.capstone2025.team4.backend.controller.dto.element.ElementType;
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
    private final ElementType type = ElementType.TEXT_BOX;

    public static TextBoxResponse createFrom(TextBox textBox) {
        TextBoxResponse textBoxDTO = new TextBoxResponse();
        textBoxDTO.text = textBox.getText();
        textBoxDTO.size = textBox.getSize();
        textBoxDTO.weight = textBox.getWeight();
        return textBoxDTO;
    }
}
