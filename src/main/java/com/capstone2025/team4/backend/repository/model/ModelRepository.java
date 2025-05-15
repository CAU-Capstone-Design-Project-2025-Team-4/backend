package com.capstone2025.team4.backend.repository.model;

import com.capstone2025.team4.backend.domain.element.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Long> , ModelRepositoryCustom{
}
