package com.capstone2025.team4.backend.repository.animation;

import com.capstone2025.team4.backend.domain.animation.Animation;
import com.capstone2025.team4.backend.domain.animation.QAnimation;
import com.capstone2025.team4.backend.domain.design.QSlide;
import com.capstone2025.team4.backend.domain.element.QElement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.capstone2025.team4.backend.domain.animation.QAnimation.animation;
import static com.capstone2025.team4.backend.domain.design.QSlide.slide;
import static com.capstone2025.team4.backend.domain.element.QElement.element;

public class AnimationRepositoryImpl implements AnimationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public AnimationRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Animation> findAllInSlide(Long slideId) {
        return queryFactory.selectFrom(animation)
                .join(animation.element, element).fetchJoin()
                .join(element.slide, slide)
                .where(slide.id.eq(slideId)).fetch();
    }
}
