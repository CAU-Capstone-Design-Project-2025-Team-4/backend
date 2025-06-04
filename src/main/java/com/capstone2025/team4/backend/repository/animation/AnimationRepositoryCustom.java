package com.capstone2025.team4.backend.repository.animation;

import com.capstone2025.team4.backend.domain.animation.Animation;

import java.util.List;

public interface AnimationRepositoryCustom {
    List<Animation> findAllInSlide(Long slideId);
}