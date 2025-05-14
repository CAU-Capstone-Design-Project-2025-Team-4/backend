package com.capstone2025.team4.backend.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileDTO {
    private byte[] fileBytes;
    private String fileName;
}
