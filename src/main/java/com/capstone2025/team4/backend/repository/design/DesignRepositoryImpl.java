package com.capstone2025.team4.backend.repository.design;

import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.QDesign;
import com.capstone2025.team4.backend.domain.design.QSlide;
import com.capstone2025.team4.backend.domain.element.QElement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class DesignRepositoryImpl implements DesignRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public DesignRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Design> findLongDesign(Long designId) {
        QDesign design = QDesign.design;
        QSlide slide = QSlide.slide;
        QElement element = QElement.element;
        Design result = queryFactory.selectFrom(design)
                .leftJoin(design.slideList, slide).fetchJoin()
                .leftJoin(slide.slideElementList, element).fetchJoin()
                .where(design.id.eq(designId))
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
