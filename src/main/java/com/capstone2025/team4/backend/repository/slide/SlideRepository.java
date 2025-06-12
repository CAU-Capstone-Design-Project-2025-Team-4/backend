package com.capstone2025.team4.backend.repository.slide;

import com.capstone2025.team4.backend.domain.design.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SlideRepository extends JpaRepository<Slide, Long>, SlideRepositoryCustom{

    @Query("SELECT s FROM Slide s LEFT JOIN FETCH s.slideElementList JOIN FETCH s.design d WHERE d.id = :designId AND d.user.id = :userId")
    List<Slide> findAllByDesignIdAndUserId(Long designId, Long userId);

    boolean existsByDesignIdAndOrder(Long designId, Integer order);

    @Query("SELECT s FROM Slide s JOIN s.design d WHERE s.id = :slideId AND d.id = :designId AND d.user.id = :userId")
    Optional<Slide> findByIdAndDesignIdAndUserId(Long slideId, Long designId, Long userId);

    @Modifying
    @Query("UPDATE Slide s SET s.order = s.order - 1 WHERE s.design.id = :designId AND s.order > :deletedOrder")
    void reorderSlidesAfterDeletion(Long designId, int deletedOrder);
}
