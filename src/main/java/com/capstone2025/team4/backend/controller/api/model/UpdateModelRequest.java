package com.capstone2025.team4.backend.controller.api.model;

import com.capstone2025.team4.backend.domain.element.model.ModelShader;
import com.capstone2025.team4.backend.domain.element.model.ModelTransform;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateModelRequest {

    @NotNull(message = "모델 정보는 필수입니다")
    private Long modelId;

    @NotNull(message = "씬 정보는 필수입니다")
    private Long spatialId;

    @NotNull(message = "유저 정보는 필수입니다")
    private Long userId;

    private String name;

    @NotNull(message = "세이더 정보는 필수입니다")
    private ModelShader shader;

    @NotNull(message = "모델의 위치, 회전, 크기 정보는 필수입니다")
    private ModelTransform modelTransform;
}
