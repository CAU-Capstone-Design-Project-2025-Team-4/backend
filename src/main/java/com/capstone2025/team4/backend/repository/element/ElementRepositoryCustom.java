package com.capstone2025.team4.backend.repository.element;

import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.domain.element.Shape;
import com.capstone2025.team4.backend.domain.element.TextBox;
import com.capstone2025.team4.backend.domain.element.border.BorderRef;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;

import java.util.Optional;

public interface ElementRepositoryCustom {
    Optional<TextBox> findTextBoxByIdAndUserId(Long id, Long userId);

    Optional<Shape> findShapeByIdAndUserId(Long id, Long userId);

    Optional<Element> findElementById(Long id, Long userId);

    Optional<Spatial> findSpatialById(Long id, Long userId);
}
