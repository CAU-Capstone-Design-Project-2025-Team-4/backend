package com.capstone2025.team4.backend.controller.dto.element.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
@Setter
public class AddImageRequest extends ElementRequest{

    @NotNull(message = "파일은 필수입니다")
    private MultipartFile image;
}
