package com.capstone2025.team4.backend.repository.element;

import com.capstone2025.team4.backend.domain.element.Element;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementRepository extends JpaRepository<Element, Long>, ElementRepositoryCustom{
}
