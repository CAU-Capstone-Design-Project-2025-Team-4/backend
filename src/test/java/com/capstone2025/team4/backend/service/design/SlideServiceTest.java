package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.domain.element.TextBox;
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
class SlideServiceTest {

    @Autowired
    SlideService slideService;

    @Autowired
    EntityManager em;

    @Test
    void newSlide(){
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();

        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);

        em.flush();
        em.clear();

        //when
        Slide slide = slideService.newSlide(testUser, testDesign, testWorkspace, 2);

        //then
        assertThat(slide.getOrder()).isEqualTo(2);
        assertThat(slide.getDesign().getId()).isEqualTo(testDesign.getId());
        assertThat(slide.getSlideElementList()).isNull();
        assertThat(slide.getId()).isNotNull();
    }

    @Test
    void copySlide(){
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

        ArrayList<Slide> destSlideList = new ArrayList<>();
        Design destDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .slideList(destSlideList)
                .build();
        Slide destSlide = Slide.builder().design(destDesign).build();
        destSlideList.add(destSlide);

        em.persist(e1);
        em.persist(e2);
        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(s1);
        em.persist(sourceDesign);
        em.persist(destDesign);
        em.persist(destSlide);

        em.flush();
        em.clear();

        //when
        Slide destSlideCopied = slideService.copySlide(destSlide.getId(), s1.getId(), testUser.getId());

        //then
        assertThat(destSlideCopied.getSlideElementList()).isNotNull();
        assertThat(destSlideCopied.getSlideElementList().size()).isEqualTo(s1.getSlideElementList().size());
        assertThat(destSlideCopied.getSlideElementList()).doesNotContain(e1, e2);
    }

    @Test
    void findAllInDesign(){
        //given
        User user = User.builder().build();
        Design design = Design.builder()
                .user(user)
                .build();

        Slide s1 = Slide.builder()
                .design(design)
                .build();

        Slide s2 = Slide.builder()
                .design(design)
                .build();

        em.persist(user);
        em.persist(design);
        em.persist(s1);
        em.persist(s2);

        em.flush();
        em.clear();

        //when
        List<Slide> allInDesign = slideService.findAllInDesign(user.getId(), design.getId());

        //then
        assertThat(allInDesign).isNotNull();
        assertThat(allInDesign).isNotEmpty();
        assertThat(allInDesign.size()).isEqualTo(2);
    }
}