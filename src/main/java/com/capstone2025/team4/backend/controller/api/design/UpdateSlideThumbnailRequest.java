package com.capstone2025.team4.backend.controller.api.design;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class UpdateSlideThumbnailRequest {
    @NotNull(message = "유저 정보는 필수입니다")
    private Long userId;

    @NotNull(message = "디자인 정보는 필수입니다")
    private Long designId;

    @NotNull(message = "슬라이드 정보는 필수입니다")
    private Long slideId;

    @NotNull(message = "이미지 파일은 필수입니다")
    private MultipartFile image;
}
