package com.capstone2025.team4.backend.repository;

import com.capstone2025.team4.backend.domain.matching.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

    @Query("SELECT m from Matching m JOIN FETCH m.receiver WHERE m.id = :id")
    Optional<Matching> findByIdWithReceiver(Long id);
}
