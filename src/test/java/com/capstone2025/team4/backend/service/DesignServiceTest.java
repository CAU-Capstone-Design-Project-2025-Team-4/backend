package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.*;
import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.domain.element.TextBox;
import com.capstone2025.team4.backend.exception.design.DesignNotShared;
import com.capstone2025.team4.backend.service.design.DesignService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DesignServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    DesignService designService;

    @Test
    void createNewDesignTestUserWorkspace() {
        // given
        User userWithoutWorkspace = User.builder()
                .email("noWorkspace")
                .build();

        User userWithWorkspace = User.builder()
                .email("withWorkspace")
                .build();
        Workspace workspace = new Workspace(userWithWorkspace);

        em.persist(userWithoutWorkspace);
        em.persist(userWithWorkspace);
        em.persist(workspace);

        em.flush();
        em.clear();

        // when
        Design succeed = designService.createNewDesign(userWithWorkspace.getId(), null, false);

        // then
        assertThat(succeed.getId()).isNotNull();

        assertThrowsExactly(RuntimeException.class, () -> designService.createNewDesign(userWithoutWorkspace.getId(), null, false), "워크스페이스 없는 사용자가 디자인을 생성했으나 예외가 발생하지 않음");
    }

    @Test
    void newDesignFromSource(){
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();

        Workspace testWorkspace = new Workspace(testUser);

        TextBox e1 = TextBox.builder().text("temp1").build();
        TextBox e2 = TextBox.builder().text("temp2").build();

        ArrayList<Element> slideElementList = new ArrayList<>();

        List<Slide> slideList = new ArrayList<>();

        Design sourceDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .slideList(slideList)
                .shared(true)
                .build();

        Slide s1 = Slide.builder().slideElementList(slideElementList).design(sourceDesign).build();
        slideList.add(s1);
        e1.addToSlide(s1);
        e2.addToSlide(s1);

        em.persist(e1);
        em.persist(e2);
        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(s1);
        em.persist(sourceDesign);

        em.flush();
        em.clear();

        //when
        Design newDesign = designService.createNewDesign(testUser.getId(), sourceDesign.getId(), false);

        //then
        assertThat(newDesign.getSlideList().size()).isEqualTo(sourceDesign.getSlideList().size());
        assertThat(newDesign.getSlideList().getFirst().getId()).isNotEqualTo(s1.getId());
        assertThat(newDesign.getSlideList().getFirst().getSlideElementList().size()).isEqualTo(sourceDesign.getSlideList().getFirst().getSlideElementList().size());
    }

    @Test
    void newDesignFromSourceError(){
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();

        Workspace testWorkspace = new Workspace(testUser);

        Design sourceDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .shared(false)
                .build();

        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(sourceDesign);

        em.flush();
        em.clear();

        //when

        //then
        assertThrowsExactly(DesignNotShared.class, () -> {designService.createNewDesign(testUser.getId(), sourceDesign.getId(), false);});
    }

    @Test
    void newDesignFromScratch(){
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();

        Workspace testWorkspace = new Workspace(testUser);

        em.persist(testUser);
        em.persist(testWorkspace);

        em.flush();
        em.clear();

        //when
        Design succeed = designService.createNewDesign(testUser.getId(), null, false);

        //then
        assertThat(succeed.getId()).isNotNull();
        assertThat(succeed.getWorkspace().getId()).isEqualTo(testWorkspace.getId());
        assertThat(succeed.getSlideList().size()).isEqualTo(1);
    }

    @Test
    void findAll(){
        //given
        User user = User.builder()
                .build();
        Design design1 = Design.builder()
                .user(user)
                .build();
        Design design2 = Design.builder()
                .user(user)
                .build();
        Design design3 = Design.builder()
                .user(user)
                .build();

        em.persist(user);
        em.persist(design1);
        em.persist(design2);
        em.persist(design3);

        em.flush();
        em.clear();

        //when
        List<Design> all = designService.findAll(user.getId());
        List<Long> idList = all.stream().map(Design::getId).toList();

        //then
        assertThat(all).isNotEmpty();
        assertThat(all.size()).isEqualTo(3);
        assertThat(idList).contains(design1.getId(), design2.getId(), design3.getId());

    }
}