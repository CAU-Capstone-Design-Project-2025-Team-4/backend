package com.capstone2025.team4.backend.repository.animation3d;

import com.capstone2025.team4.backend.domain.animation3d.Animation3D;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.capstone2025.team4.backend.domain.animation3d.QAnimation3D.animation3D;
import static com.capstone2025.team4.backend.domain.design.QSlide.slide;
import static com.capstone2025.team4.backend.domain.element.spatial.QSpatial.spatial;

public class Animation3dRepositoryImpl implements Animation3dRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public Animation3dRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Animation3D> findAllInSlide(Long slideId) {
        return queryFactory.selectFrom(animation3D)
                .join(animation3D.spatial, spatial).fetchJoin()
                .join(spatial.slide, slide)
                .where(slide.id.eq(slideId)).fetch();
    }
}
