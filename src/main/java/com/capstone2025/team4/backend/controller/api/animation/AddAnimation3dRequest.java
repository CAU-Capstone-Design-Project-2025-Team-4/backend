package com.capstone2025.team4.backend.controller.api.animation;

import com.capstone2025.team4.backend.domain.animation3d.Animation3dType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AddAnimation3dRequest {
    @NotNull(message = "사용자 정보는 필수입니다")
    private Long userId;

    @NotNull(message = "요소 정보는 필수입니다")
    private Long elementId;

    @NotNull(message = "애니메이션 타입 정보는 필수입니다")
    private Animation3dType type;
}
