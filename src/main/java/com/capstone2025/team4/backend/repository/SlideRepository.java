package com.capstone2025.team4.backend.repository;

import com.capstone2025.team4.backend.domain.design.Slide;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SlideRepository extends JpaRepository<Slide, Long> {

    @EntityGraph(attributePaths = {"slideElementList"})
    Optional<Slide> findWithSlideElementListById(Long id);
}
