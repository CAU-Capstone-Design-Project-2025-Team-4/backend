package com.capstone2025.team4.backend.controller.dto.design;

import com.capstone2025.team4.backend.domain.design.SlideElement;
import com.capstone2025.team4.backend.domain.design.element.Element;
import com.capstone2025.team4.backend.domain.design.element.FileElement;
import lombok.Getter;

@Getter
public class FileElementResponse {
    private Long id;
    private String url;
    private Long x;
    private Long y;
    private Long width;
    private Long height;
    private Double angle;

    public FileElementResponse(SlideElement slideElement) {
        this.id = slideElement.getId();
        FileElement fileElement = (FileElement) slideElement.getElement();
        this.url = fileElement.getS3Url();
        this.x = slideElement.getX();
        this.y = slideElement.getY();
        this.width = slideElement.getWidth();
        this.height = slideElement.getHeight();
        this.angle = slideElement.getAngle();
    }
}
