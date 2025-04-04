package com.capstone2025.team4.backend.repository.design;

import com.capstone2025.team4.backend.domain.design.Design;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignRepository extends JpaRepository<Design, Long>, DesignRepositoryCustom {
    List<Design> findAllByUser_Id(Long userId);
}
