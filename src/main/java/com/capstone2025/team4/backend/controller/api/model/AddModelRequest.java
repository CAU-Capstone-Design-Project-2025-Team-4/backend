package com.capstone2025.team4.backend.controller.api.model;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AddModelRequest {
    @NotNull(message = "씬 정보는 필수입니다")
    private Long spatialId;
    @NotNull(message = "유저 정보는 필수입니다")
    private Long userId;
    @NotNull(message = "파일은 필수입니다")
    private MultipartFile file;
}
