package com.capstone2025.team4.backend.repository.animation3d;

import com.capstone2025.team4.backend.domain.animation3d.Animation3D;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Animation3dRepository extends JpaRepository<Animation3D, Long>, Animation3dRepositoryCustom{

    boolean existsByIdAndSpatialId(Long id, Long spatialId);

    List<Animation3D> findAllBySpatialId(Long spatialId);

    @EntityGraph(attributePaths = {"spatial"})
    Optional<Animation3D> findWithElementById(Long id);
}
