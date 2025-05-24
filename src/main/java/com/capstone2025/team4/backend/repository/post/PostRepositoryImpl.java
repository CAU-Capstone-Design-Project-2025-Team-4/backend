package com.capstone2025.team4.backend.repository.post;

import com.capstone2025.team4.backend.service.dto.PostFullDTO;
import com.capstone2025.team4.backend.service.dto.PostSimpleDTO;
import com.capstone2025.team4.backend.service.dto.QPostFullDTO;
import com.capstone2025.team4.backend.service.dto.QPostSimpleDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.capstone2025.team4.backend.domain.QPost.post;
import static com.capstone2025.team4.backend.domain.QUser.user;
import static com.capstone2025.team4.backend.domain.design.QDesign.design;

public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PostSimpleDTO> searchPage(Pageable pageable, Long userId) {
        List<PostSimpleDTO> posts = queryFactory
                .select(new QPostSimpleDTO(post.id, user.email, post.title, post.createdAt))
                .from(post)
                .leftJoin(post.user, user)
                .where(
                        userIdEq(userId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }

    public PostFullDTO findFullPost(Long postId) {
        return queryFactory
                .select(new QPostFullDTO(post.id, user.email, design.id, post.createdAt, post.title, post.content))
                .from(post)
                .leftJoin(post.user, user)
                .leftJoin(post.design, design)
                .fetchOne();
    }
}
