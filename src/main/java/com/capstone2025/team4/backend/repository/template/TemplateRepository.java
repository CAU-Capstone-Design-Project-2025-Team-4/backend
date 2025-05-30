package com.capstone2025.team4.backend.repository.template;

import com.capstone2025.team4.backend.domain.design.Design;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Design, Long>, TemplateRepositoryCustom {
}