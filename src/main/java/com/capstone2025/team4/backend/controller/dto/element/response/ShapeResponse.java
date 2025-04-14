package com.capstone2025.team4.backend.controller.dto.element.response;

import com.capstone2025.team4.backend.controller.dto.element.ElementType;
import com.capstone2025.team4.backend.domain.element.Shape;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShapeResponse extends ElementResponse {
    private String path;
    private String color;
    private final ElementType type = ElementType.SHAPE;

    public static ShapeResponse createFrom(Shape shape) {
        ShapeResponse shapeDTO = new ShapeResponse();
        shapeDTO.path = shape.getPath();
        shapeDTO.color = shape.getColor();
        return shapeDTO;
    }
}
