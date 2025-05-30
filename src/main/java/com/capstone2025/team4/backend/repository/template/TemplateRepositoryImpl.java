package com.capstone2025.team4.backend.repository.template;

import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.service.dto.QTemplateLongDTO;
import com.capstone2025.team4.backend.service.dto.QTemplateShortDTO;
import com.capstone2025.team4.backend.service.dto.TemplateLongDTO;
import com.capstone2025.team4.backend.service.dto.TemplateShortDTO;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.capstone2025.team4.backend.domain.QUser.user;
import static com.capstone2025.team4.backend.domain.design.QDesign.design;
import static com.capstone2025.team4.backend.domain.design.QSlide.slide;
import static com.querydsl.core.group.GroupBy.*;

public class TemplateRepositoryImpl implements TemplateRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public TemplateRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT,em);
    }

    @Override
    public List<TemplateShortDTO> findAllTemplates() {
        return queryFactory
                .select(new QTemplateShortDTO(design.id, design.name, design.thumbnail))
                .from(design)
                .where(design.shared.isTrue())
                .fetch();
    }

    @Override
    public Optional<TemplateLongDTO> findTemplate(Long id) {
        Map<Long, TemplateLongDTO> transform = queryFactory
                .select(design.id, design.name, user.name, design.thumbnail, slide.order, slide.thumbnail)
                .from(design)
                .leftJoin(design.user, user)
                .leftJoin(design.slideList, slide)
                .where(design.id.eq(id))
                .orderBy(slide.order.asc())
                .transform(groupBy(design.id).as(
                        new QTemplateLongDTO(
                                design.id,
                                design.name,
                                user.name,
                                design.thumbnail,
                                map(slide.order, slide.thumbnail)
                        )
                ));
        TemplateLongDTO dto = transform.get(id);
        return Optional.ofNullable(dto);
    }
}
