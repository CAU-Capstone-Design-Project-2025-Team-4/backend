package com.capstone2025.team4.backend.controller.dto.element.response;

import com.capstone2025.team4.backend.controller.dto.element.ElementType;
import com.capstone2025.team4.backend.domain.element.Image;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageResponse extends ElementResponse {
    private String content;
    private final ElementType type = ElementType.IMAGE;

    public static ImageResponse createFrom(Image image) {
        ImageResponse imageDTO = new ImageResponse();
        imageDTO.content = image.getContent();
        return imageDTO;
    }
}
