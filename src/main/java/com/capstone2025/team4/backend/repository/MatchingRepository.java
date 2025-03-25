package cau.capstone2025.team4.repository;

import cau.capstone2025.team4.domain.matching.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
}
