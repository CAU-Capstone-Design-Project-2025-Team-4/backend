package com.capstone2025.team4.backend.repository;

import com.capstone2025.team4.backend.domain.design.element.Element;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementRepository extends JpaRepository<Element, Long> {
}