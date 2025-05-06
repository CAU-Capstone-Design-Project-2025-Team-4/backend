package com.capstone2025.team4.backend.repository;

import com.capstone2025.team4.backend.domain.design.Slide;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SlideRepository extends JpaRepository<Slide, Long> {

    @EntityGraph(attributePaths = {"slideElementList"})
    Optional<Slide> findWithSlideElementListById(Long id);

    @Query("SELECT s FROM Slide s LEFT JOIN FETCH s.slideElementList JOIN FETCH s.design d WHERE d.id = :designId AND d.user.id = :userId")
    List<Slide> findAllByDesignIdAndUserId(Long designId, Long userId);
}
