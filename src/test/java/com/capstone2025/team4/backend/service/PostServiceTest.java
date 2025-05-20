package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.Post;
import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.design.Design;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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
                .build();
        Design design = Design.builder().user(user).build();
        final String TITLE = "TITLE";
        final String CONTENT = "CONTENT";

        em.persist(user);
        em.persist(design);
        em.flush();
        em.clear();

        //when
        Post newPost = postService.createNewPost(user.getId(), design.getId(), TITLE, CONTENT);
        em.flush();
        em.clear();

        //then
        Post foundPost = em.find(Post.class, newPost.getId());
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getTitle()).isEqualTo(TITLE);
        assertThat(foundPost.getContent()).isEqualTo(CONTENT);
        assertThat(foundPost.getUser().getId()).isEqualTo(user.getId());
        assertThat(foundPost.getDesign().getId()).isEqualTo(design.getId());
    }
}