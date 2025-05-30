package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.exception.template.TemplateNotFound;
import com.capstone2025.team4.backend.repository.template.TemplateRepository;
import com.capstone2025.team4.backend.service.dto.TemplateLongDTO;
import com.capstone2025.team4.backend.service.dto.TemplateShortDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/template")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateRepository templateRepository;

    @GetMapping
    public ApiResponse<List<TemplateShortDTO>> findAll() {
        List<TemplateShortDTO> result= templateRepository.findAllTemplates();
        return ApiResponse.success(result);
    }

    @GetMapping("/{templateId}")
    public ApiResponse<TemplateLongDTO> find(@PathVariable Long templateId) {
        Optional<TemplateLongDTO> optionalTemplate = templateRepository.findTemplate(templateId);
        if (optionalTemplate.isEmpty()) {
            throw new TemplateNotFound();
        }

        return ApiResponse.success(optionalTemplate.get());
    }
}
