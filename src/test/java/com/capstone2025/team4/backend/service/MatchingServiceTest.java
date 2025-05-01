package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.matching.Matching;
import com.capstone2025.team4.backend.domain.matching.Status;
import com.capstone2025.team4.backend.domain.post.Post;
import com.capstone2025.team4.backend.exception.user.UserNotAllowed;
import jakarta.persistence.EntityManager;
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
        assertThat(matching.getStatus()).isEqualTo(Status.WAITING);
        assertThat(matching.getSender().getId()).isEqualTo(sender.getId());
        assertThat(matching.getReceiver().getId()).isEqualTo(receiver.getId());
        assertThat(matching.getPost().getId()).isEqualTo(post.getId());
    }

    @Test
    void rejectMatching(){
        //given
        User sender = User.builder()
                .build();

        User receiver = User.builder()
                .build();

        Post post = Post.builder()
                .designer(receiver)
                .build();

        Matching matching = Matching.builder()
                .sender(sender)
                .receiver(receiver)
                .post(post)
                .build();

        em.persist(sender);
        em.persist(receiver);
        em.persist(post);
        em.persist(matching);

        em.flush();
        em.clear();

        //when
        Matching resultMatching = matchingService.rejectMatching(receiver.getId(), matching.getId());

        //then
        assertThat(resultMatching).isNotNull();
        assertThat(resultMatching.getStatus()).isEqualTo(Status.REJECTED);
        assertThrows(UserNotAllowed.class, ()->{
            matchingService.rejectMatching(sender.getId(), matching.getId());});

    }

    @Test
    void acceptMatching(){
        //given
        User sender = User.builder()
                .build();

        User receiver = User.builder()
                .build();

        Post post = Post.builder()
                .designer(receiver)
                .build();

        Matching matching = Matching.builder()
                .sender(sender)
                .receiver(receiver)
                .post(post)
                .build();

        em.persist(sender);
        em.persist(receiver);
        em.persist(post);
        em.persist(matching);

        em.flush();
        em.clear();

        //when
        Matching resultMatching = matchingService.acceptMatching(receiver.getId(), matching.getId());

        //then
        assertThat(resultMatching).isNotNull();
        assertThat(resultMatching.getStatus()).isEqualTo(Status.ACCEPTED);
        assertThrows(UserNotAllowed.class, ()->{
            matchingService.rejectMatching(sender.getId(), matching.getId());});

    }
}
