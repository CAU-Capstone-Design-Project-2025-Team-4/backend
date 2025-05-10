package com.capstone2025.team4.backend.repository.design;

import com.capstone2025.team4.backend.domain.design.Design;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DesignRepository extends JpaRepository<Design, Long>, DesignRepositoryCustom {
    List<Design> findAllByUserId(Long userId);

    @Query("SELECT count(d) > 0 FROM Design d JOIN d.user u WHERE d.id = :designId AND u.id = :userId ")
    boolean exists(Long designId, Long userId);}
