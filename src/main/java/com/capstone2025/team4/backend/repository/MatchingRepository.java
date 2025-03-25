package com.capstone2025.team4.backend.repository;

import com.capstone2025.team4.backend.domain.matching.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
}
