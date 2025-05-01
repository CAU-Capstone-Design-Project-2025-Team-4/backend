package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.matching.Matching;
import com.capstone2025.team4.backend.domain.post.Post;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MatchingServiceTest {

    @Autowired
    MatchingService matchingService;

    @Autowired
    EntityManager em;

    @Test
    void requestMatching() {
        // given
        User sender = User.builder()
                .build();

        User receiver = User.builder()
                .build();

        Post post = Post.builder()
                .designer(receiver)
                .build();

        em.persist(sender);
        em.persist(receiver);
        em.persist(post);

        em.flush();
        em.clear();

        // when
        Matching matching = matchingService.requestMatching(sender.getId(), receiver.getId(), post.getId());

        // then
        assertThat(matching).isNotNull();
        assertThat(matching.getSender().getId()).isEqualTo(sender.getId());
        assertThat(matching.getReceiver().getId()).isEqualTo(receiver.getId());
        assertThat(matching.getPost().getId()).isEqualTo(post.getId());
    }
}
