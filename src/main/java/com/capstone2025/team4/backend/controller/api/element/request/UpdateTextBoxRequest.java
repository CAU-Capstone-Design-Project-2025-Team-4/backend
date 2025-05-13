package com.capstone2025.team4.backend.controller.api.element.request;

import com.capstone2025.team4.backend.domain.element.TextAlign;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateTextBoxRequest {
    @NotNull(message = "사용자 정보는 필수입니다")
    Long userId;
    @NotNull(message = "요소 정보는 필수입니다")
    Long elementId;
    @NotNull(message = "텍스트 정보는 필수입니다")
    String text;
    @NotNull(message = "크기 정보는 필수입니다")
    Long size;
    @NotNull(message = "굵기 정보는 필수입니다")
    Long weight;
    @NotNull(message = "폰트 패밀리 정보는 필수입니다")
    @NotBlank(message = "폰트 패밀리 정보가 비어 있습니다")
    String fontFamily;
    @NotNull(message = "텍스트 정렬 정보는 필수입니다")
    TextAlign textAlign;
}
