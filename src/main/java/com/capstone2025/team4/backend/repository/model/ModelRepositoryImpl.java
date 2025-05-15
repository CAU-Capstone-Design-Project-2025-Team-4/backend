package com.capstone2025.team4.backend.repository.model;

import com.capstone2025.team4.backend.domain.QUser;
import com.capstone2025.team4.backend.domain.design.QDesign;
import com.capstone2025.team4.backend.domain.design.QSlide;
import com.capstone2025.team4.backend.domain.element.model.Model;
import com.capstone2025.team4.backend.domain.element.model.QModel;
import com.capstone2025.team4.backend.domain.element.spatial.QSpatial;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class ModelRepositoryImpl implements ModelRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public ModelRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Model> findByIdAndSpatialAndUser(Long modelId, Long spatialId, Long userId) {
        QModel model = QModel.model;
        QSpatial spatial = QSpatial.spatial;
        QSlide slide = QSlide.slide;
        QDesign design = QDesign.design;
        QUser user = QUser.user;

        Model found = queryFactory
                .selectFrom(model)
                .join(model.spatial, spatial)
                .join(spatial.slide, slide)
                .join(slide.design, design)
                .join(design.user, user)
                .where(
                        model.id.eq(modelId),
                        spatial.id.eq(spatialId),
                        user.id.eq(userId)
                )
                .fetchOne();
        return Optional.ofNullable(found);
    }
}
