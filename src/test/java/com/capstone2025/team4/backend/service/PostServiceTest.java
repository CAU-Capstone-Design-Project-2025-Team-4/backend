package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.post.Category;
import com.capstone2025.team4.backend.domain.post.Post;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    PostService postService;

    @Test
    void createNewPost() {
        //given
        User user = User.builder()
                .name("temp")
                .email("tempEmail")
                .password("temp")
                .build();
        em.persist(user);
        em.flush();
        em.clear();

        //when
        Post badUserId = postService.createNewPost(123123123L, "c", "d", Category.SERVICE);
        Post emptyTitlePost = postService.createNewPost(user.getId(), "", "d", Category.SERVICE);
        Post emptyDescription = postService.createNewPost(user.getId(), "t", "", Category.SERVICE);
        Post emptyCategory = postService.createNewPost(user.getId(), "t", "c", null);
        Post newPost = postService.createNewPost(user.getId(), "title", "desc", Category.SELF_INTRO);

        //then
        assertThat(new Post[]{badUserId, emptyTitlePost, emptyDescription, emptyCategory}).containsOnlyNulls();
        assertThat(newPost).isNotNull();
        assertThat(newPost.getId()).isNotNull();
    }
}