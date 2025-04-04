package com.capstone2025.team4.backend.controller.dto.element;

import com.capstone2025.team4.backend.domain.design.SlideElement;
import com.capstone2025.team4.backend.domain.design.Type;
import com.capstone2025.team4.backend.domain.design.element.Element;
import com.capstone2025.team4.backend.domain.design.element.FileElement;
import com.capstone2025.team4.backend.domain.design.element.TextElement;
import lombok.Getter;

@Getter
public class ElementResponse {
    private Long id;
    private Type type;
    private String content;
    private Long x;
    private Long y;
    private Long width;
    private Long height;
    private Double angle;

    public ElementResponse(Element element) {
        this.id = element.getId();
        Type type = element.getType();
        this.type = element.getType();
        if (type == Type.MODEL || type == Type.IMAGE) {
            FileElement fileElement = (FileElement) element;
            this.content = fileElement.getS3Url();
        } else {
            TextElement textElement = (TextElement) element;
            this.content = textElement.getText();
        }
        this.x = element.getX();
        this.y = element.getY();
        this.width = element.getWidth();
        this.height = element.getHeight();
        this.angle = element.getAngle();
    }

    public ElementResponse(SlideElement slideElement) {
        this.id = slideElement.getId();
        Element element = slideElement.getElement();
        Type type = element.getType();
        this.type = element.getType();
        if (type == Type.MODEL || type == Type.IMAGE) {
            FileElement fileElement = (FileElement) element;
            this.content = fileElement.getS3Url();
        } else {
            TextElement textElement = (TextElement) element;
            this.content = textElement.getText();
        }
        this.x = slideElement.getX();
        this.y = slideElement.getY();
        this.width = slideElement.getWidth();
        this.height = slideElement.getHeight();
        this.angle = slideElement.getAngle();
    }
}
