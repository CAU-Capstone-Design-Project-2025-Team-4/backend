package com.capstone2025.team4.backend.repository.slide;

import com.capstone2025.team4.backend.domain.design.Slide;

import java.util.Optional;

public interface SlideRepositoryCustom {
    Optional<Slide> findSlide(Long userId, Long slideId);
    Optional<Slide> findSlideWithElements(Long userId, Long slideId);
    Optional<Slide> findSlideWithElements(Long slideId);

    Optional<Slide> findSlideWithElementsSharedDesign(Long slideId);
}
