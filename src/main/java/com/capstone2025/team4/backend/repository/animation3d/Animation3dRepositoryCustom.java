package com.capstone2025.team4.backend.repository.animation3d;

import com.capstone2025.team4.backend.domain.animation3d.Animation3D;

import java.util.List;

public interface Animation3dRepositoryCustom {
    List<Animation3D> findAllInSlide(Long slideId);
}
