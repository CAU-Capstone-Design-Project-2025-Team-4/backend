package com.capstone2025.team4.backend.repository.frame;

import com.capstone2025.team4.backend.domain.element.spatial.Frame;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FrameRepository extends JpaRepository<Frame, Long> {
    void deleteByIdAndSpatial(Long id, Spatial spatial);

    Optional<Frame> findByIdAndSpatial(Long id, Spatial spatial);

    List<Frame> findAllBySpatial(Spatial spatial);
}
