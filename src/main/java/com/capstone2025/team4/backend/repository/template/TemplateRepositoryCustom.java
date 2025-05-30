package com.capstone2025.team4.backend.repository.template;

import com.capstone2025.team4.backend.service.dto.TemplateLongDTO;
import com.capstone2025.team4.backend.service.dto.TemplateShortDTO;

import java.util.List;
import java.util.Optional;

public interface TemplateRepositoryCustom {
    List<TemplateShortDTO> findAllTemplates();

    Optional<TemplateLongDTO> findTemplate(Long id);
}
