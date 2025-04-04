package com.capstone2025.team4.backend.repository.design;

import com.capstone2025.team4.backend.domain.design.Design;

import java.util.Optional;

public interface DesignRepositoryCustom {
    Optional<Design> findLongDesign(Long designId);
}
