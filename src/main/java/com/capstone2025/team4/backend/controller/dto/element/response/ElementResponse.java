package com.capstone2025.team4.backend.controller.dto.element.response;

import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.domain.element.Image;
import com.capstone2025.team4.backend.domain.element.Shape;
import com.capstone2025.team4.backend.domain.element.TextBox;
import com.capstone2025.team4.backend.domain.element.border.BorderRef;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class ElementResponse {
    @NotNull(message = "요소 아이디는 필수 값입니다")
    private Long id;
    private BorderRef borderRef;
    private Long x;
    private Long y;
    private Long z;
    private Long width;
    private Long height;
    private Double angle;

    public static ElementResponse create(Element element) {
        ElementResponse result;
        if (element instanceof Image image) {
            result = ImageResponse.createFrom(image);
        } else if (element instanceof TextBox textBox) {
            result = TextBoxResponse.createFrom(textBox);
        } else if (element instanceof Shape shape) {
            result = ShapeResponse.createFrom(shape);
        } else if (element instanceof Spatial spatial) {
            result = SpatialResponse.createFrom(spatial);
        } else {
            throw new IllegalArgumentException("Unknown element type: " + element.getClass());
        }
        result.setCommonFields(element);
        return result;
    }

    protected final void setCommonFields(Element element) {
        this.id = element.getId();
        this.borderRef = element.getBorderRef();
        this.x = element.getX();
        this.y = element.getY();
        this.z = element.getZ();
        this.width = element.getWidth();
        this.height = element.getHeight();
        this.angle = element.getAngle();
    }
}
