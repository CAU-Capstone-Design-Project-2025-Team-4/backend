package com.capstone2025.team4.backend.repository.slide;

import com.capstone2025.team4.backend.domain.design.Slide;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.capstone2025.team4.backend.domain.QUser.user;
import static com.capstone2025.team4.backend.domain.design.QDesign.design;
import static com.capstone2025.team4.backend.domain.design.QSlide.slide;
import static com.capstone2025.team4.backend.domain.element.QElement.element;

public class SlideRepositoryImpl implements SlideRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SlideRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Slide> findSlide(Long userId, Long slideId) {
        Slide result = queryFactory
                .selectFrom(slide)
                .join(slide.design, design)
                .where(slide.id.eq(slideId)
                        .and(design.user.id.eq(userId))
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Slide> findSlideWithElements(Long userId, Long slideId) {
        Slide result = queryFactory
                .selectDistinct(slide)
                .from(slide)
                .join(slide.design, design)
                .join(design.user, user)
                .leftJoin(slide.slideElementList, element).fetchJoin()
                .where(slide.id.eq(slideId)
                        .and(user.id.eq(userId))
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Slide> findSlideWithElements(Long slideId) {
        Slide result = queryFactory
                .selectDistinct(slide)
                .from(slide)
                .leftJoin(slide.slideElementList, element).fetchJoin()
                .where(slide.id.eq(slideId))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Slide> findSlideWithElementsSharedDesign(Long slideId) {
        Slide result = queryFactory
                .selectDistinct(slide)
                .from(slide)
                .leftJoin(slide.slideElementList, element).fetchJoin()
                .join(slide.design, design)
                .where(slide.id.eq(slideId).and(design.shared.isTrue()))
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
