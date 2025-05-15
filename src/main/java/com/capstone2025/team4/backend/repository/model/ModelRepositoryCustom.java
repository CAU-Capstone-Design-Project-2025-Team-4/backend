package com.capstone2025.team4.backend.repository.model;

import com.capstone2025.team4.backend.domain.element.model.Model;

import java.util.Optional;

public interface ModelRepositoryCustom {
    Optional<Model> findByIdAndSpatialAndUser(Long modelId, Long spatialId, Long userId);
}
