package com.capstone2025.team4.backend.domain.design;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Color {
    private Integer red;
    private Integer green;
    private Integer blue;

    static public Color getNewDefaultColor() {
        Color color = new Color();
        color.red = 0;
        color.green = 0;
        color.blue = 0;

        return color;
    }

    public Color getCopy(){
        Color color = new Color();
        color.red = this.red;
        color.green = this.green;
        color.blue = this.blue;

        return color;
    }
}
