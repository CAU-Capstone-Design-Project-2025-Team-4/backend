package com.capstone2025.team4.backend.repository.element;

import com.capstone2025.team4.backend.domain.QUser;
import com.capstone2025.team4.backend.domain.design.QDesign;
import com.capstone2025.team4.backend.domain.design.QSlide;
import com.capstone2025.team4.backend.domain.element.*;
import com.capstone2025.team4.backend.domain.element.spatial.QSpatial;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class ElementRepositoryImpl implements ElementRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ElementRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<TextBox> findTextBoxByIdAndUserId(Long id, Long userId) {
        QTextBox textBox = QTextBox.textBox;
        QSlide slide = QSlide.slide;
        QDesign design = QDesign.design;
        QUser user = QUser.user;

        TextBox result = queryFactory.selectFrom(textBox)
                .join(textBox.slide, slide).fetchJoin()
                .join(slide.design, design).fetchJoin()
                .join(design.user, user)
                .where(textBox.id.eq(id),user.id.eq(userId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Shape> findShapeByIdAndUserId(Long id, Long userId) {
        QShape shape = QShape.shape;
        QSlide slide = QSlide.slide;
        QDesign design = QDesign.design;
        QUser user = QUser.user;
        Shape result = queryFactory.selectFrom(shape)
                .join(shape.slide, slide).fetchJoin()
                .join(slide.design, design).fetchJoin()
                .join(design.user, user)
                .where(shape.id.eq(id), user.id.eq(userId))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Element> findElementById(Long id, Long userId) {
        QElement element = QElement.element;
        QSlide slide = QSlide.slide;
        QDesign design = QDesign.design;
        QUser user = QUser.user;

        Element result = queryFactory.selectFrom(element)
                .join(element.slide, slide)
                .join(slide.design, design)
                .join(design.user, user)
                .where(element.id.eq(id), user.id.eq(userId)).fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Spatial> findSpatialById(Long id, Long userId) {
        QSpatial spatial = QSpatial.spatial;
        QSlide slide = QSlide.slide;
        QDesign design = QDesign.design;

        Spatial result = queryFactory.selectFrom(spatial)
                .join(spatial.slide, slide).fetchJoin()
                .join(slide.design, design).fetchJoin()
                .where(spatial.id.eq(id)).fetchOne();

        return Optional.ofNullable(result);
    }
}
