package com.capstone2025.team4.backend.repository.animation;

import com.capstone2025.team4.backend.domain.animation.Animation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimationRepository extends JpaRepository<Animation, Long>, AnimationRepositoryCustom{

    boolean existsByIdAndElementId(Long id, Long elementId);

    List<Animation> findAllByElementId(Long elementId);

    @EntityGraph(attributePaths = {"element"})
    Optional<Animation> findWithElementById(Long id);
}
