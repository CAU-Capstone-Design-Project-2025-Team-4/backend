package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        Design failed = designService.createNewDesign(userWithoutWorkspace, null, false);
        Design succeed = designService.createNewDesign(userWithWorkspace, null, false);

        // then
        assertThat(failed).isNull();
        assertThat(succeed.getId()).isNotNull();
    }

    @Test
    void newDesignFromSource(){
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();

        Workspace testWorkspace = new Workspace(testUser);

        Element e1 = Element.builder().isDefault(true).build();
        Element e2 = Element.builder().isDefault(false).build();

        ArrayList<SlideElement> slideElementList = new ArrayList<>();
        SlideElement se1 = SlideElement.builder().element(e1)
                .build();
        SlideElement se2 = SlideElement.builder().element(e2)
                .build();
        slideElementList.add(se1);
        slideElementList.add(se2);

        List<Slide> slideList = new ArrayList<>();
        Slide s1 = Slide.builder().slideElementList(slideElementList).build();
        slideList.add(s1);

        Design sourceDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .slideList(slideList) // 테스트에서는 간단히 empty list
                .build();

        em.persist(e1);
        em.persist(e2);
        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(s1);
        em.persist(sourceDesign);

        em.flush();
        em.clear();

        //when
        Design newDesign = designService.createNewDesign(testUser, sourceDesign, false);

        //then
        assertThat(newDesign.getSlideList().size()).isEqualTo(sourceDesign.getSlideList().size());
        assertThat(newDesign.getSlideList().getFirst().getId()).isNotEqualTo(s1.getId());
        assertThat(newDesign.getSlideList().getFirst().getSlideElementList().size()).isEqualTo(sourceDesign.getSlideList().getFirst().getSlideElementList().size());
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
        Design succeed = designService.createNewDesign(testUser, null, false);

        //then
        assertThat(succeed.getId()).isNotNull();
        assertThat(succeed.getWorkspace().getId()).isEqualTo(testWorkspace.getId());
        assertThat(succeed.getSlideList()).isNull();
    }


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
        Slide slide = designService.newSlide(testUser, testWorkspace, testDesign, 0);

        //then
        assertThat(slide.getOrder()).isEqualTo(0);
        assertThat(slide.getDesign().getId()).isEqualTo(testDesign.getId());
        assertThat(slide.getSlideElementList()).isNull();
        assertThat(slide.getId()).isNotNull();
    }

    @Test
    void addUserElementToSlide(){
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();
        Slide testSlide = designService.newSlide(testUser, testWorkspace, testDesign, 0);

        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);

        em.flush();
        em.clear();

        //when
        User user = em.find(User.class, testUser.getId());
        Workspace workspace = em.find(Workspace.class, testWorkspace.getId());
        Design design = em.find(Design.class, testDesign.getId());
        Slide slide = em.find(Slide.class, testSlide.getId());
        SlideElement slideElement = designService.addUserElementToSlide(user, workspace, design, slide, "tempUrl", "ICON", 0L, 0L, 0.0, 10L, 10L);

        //then
        assertThat(slideElement.getId()).isNotNull();
        assertThat(slideElement.getSlide().getId()).isEqualTo(slide.getId());
        assertThat(slideElement.getSlide().getDesign().getId()).isEqualTo(design.getId());


    }

    @Test
    void addDefaultElementToSlide(){
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();
        Slide testSlide = designService.newSlide(testUser, testWorkspace, testDesign, 0);

        Element e1 = Element.builder().isDefault(true).build();
        Element e2 = Element.builder().isDefault(true).build();

        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(e1);
        em.persist(e2);

        em.flush();
        em.clear();

        //when
        SlideElement slideElement1 = designService.addDefaultElementToSlide(
                em.find(User.class, testUser.getId()),
                em.find(Workspace.class, testWorkspace.getId()),
                em.find(Design.class, testDesign.getId()),
                em.find(Slide.class, testSlide.getId()),
                e1.getId(),
                10L,
                10L,
                10.0,
                1000L,
                1000L
        );

        SlideElement slideElement2 = designService.addDefaultElementToSlide(
                em.find(User.class, testUser.getId()),
                em.find(Workspace.class, testWorkspace.getId()),
                em.find(Design.class, testDesign.getId()),
                em.find(Slide.class, testSlide.getId()),
                e2.getId(),
                20L,
                20L,
                20.0,
                2000L,
                2000L
        );

        //then
        // Assert that the slide elements have been created
        assertThat(slideElement1.getId()).isNotNull();
        assertThat(slideElement2.getId()).isNotNull();

        // Verify that each slide element is associated with the correct slide and design
        assertThat(slideElement1.getSlide().getId()).isEqualTo(testSlide.getId());
        assertThat(slideElement1.getSlide().getDesign().getId()).isEqualTo(testDesign.getId());
        assertThat(slideElement2.getSlide().getId()).isEqualTo(testSlide.getId());
        assertThat(slideElement2.getSlide().getDesign().getId()).isEqualTo(testDesign.getId());

        // Verify that the correct element is linked to each slide element
        assertThat(slideElement1.getElement().getId()).isEqualTo(e1.getId());
        assertThat(slideElement2.getElement().getId()).isEqualTo(e2.getId());

        // Optionally, verify position and size properties
        assertThat(slideElement1.getX()).isEqualTo(10.0F);
        assertThat(slideElement1.getY()).isEqualTo(10.0F);
        assertThat(slideElement1.getAngle()).isEqualTo(10.0);
        assertThat(slideElement1.getWidth()).isEqualTo(1000L);
        assertThat(slideElement1.getHeight()).isEqualTo(1000L);

        assertThat(slideElement2.getX()).isEqualTo(20.0F);
        assertThat(slideElement2.getY()).isEqualTo(20.0F);
        assertThat(slideElement2.getAngle()).isEqualTo(20.0);
        assertThat(slideElement2.getWidth()).isEqualTo(2000L);
        assertThat(slideElement2.getHeight()).isEqualTo(2000L);
    }

    @Test
    void updateSlideElement(){
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();
        Slide testSlide = designService.newSlide(testUser, testWorkspace, testDesign, 0);

        Element e = Element.builder().isDefault(true).build();

        SlideElement se = SlideElement.builder()
                .slide(testSlide)
                .element(e)
                .x(0L)
                .y(0L)
                .width(0L)
                .height(0L)
                .angle(0.0)
                .build();

        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(e);
        em.persist(se);

        em.flush();
        em.clear();

        //when
        SlideElement updatedSlideElement = designService.updateSlideElement(em.find(User.class, testUser.getId()), em.find(SlideElement.class, se.getId()), em.find(Design.class, testDesign.getId()),
                1L, 2L, 3.0, 4L, 5L
        );

        // then
        assertThat(updatedSlideElement.getId()).isEqualTo(se.getId());
        assertThat(updatedSlideElement.getSlide().getId()).isEqualTo(testSlide.getId());
        assertThat(updatedSlideElement.getElement().getId()).isEqualTo(e.getId());
        assertThat(updatedSlideElement.getX()).isEqualTo(1L);
        assertThat(updatedSlideElement.getY()).isEqualTo(2L);
        assertThat(updatedSlideElement.getAngle()).isEqualTo(3.0);
        assertThat(updatedSlideElement.getWidth()).isEqualTo(4L);
        assertThat(updatedSlideElement.getHeight()).isEqualTo(5L);


        //then

    }
}