package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Element;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.design.SlideElement;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ElementServiceTest {

    @Autowired
    ElementService elementService;
    @Autowired
    EntityManager em;

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
//        Slide testSlide = designService.newSlide(testUser, testWorkspace, testDesign, 0);
        Slide testSlide = Slide.builder()
                .order(0)
                .design(testDesign).build();

        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(testSlide);
        em.persist(testSlide);

        em.flush();
        em.clear();

        //when
        User user = em.find(User.class, testUser.getId());
        Workspace workspace = em.find(Workspace.class, testWorkspace.getId());
        Design design = em.find(Design.class, testDesign.getId());
        Slide slide = em.find(Slide.class, testSlide.getId());
        SlideElement slideElement = elementService.addUserElementToSlide(user, workspace, design, slide, "tempUrl", "ICON", 0L, 0L, 0.0, 10L, 10L);

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
//        Slide testSlide = designService.newSlide(testUser, testWorkspace, testDesign, 0);
        Slide testSlide = Slide.builder()
                .order(0)
                .design(testDesign).build();

        Element e1 = Element.builder().isDefault(true).build();
        Element e2 = Element.builder().isDefault(true).build();

        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(e1);
        em.persist(e2);
        em.persist(testSlide);

        em.flush();
        em.clear();

        //when
        SlideElement slideElement1 = elementService.addDefaultElementToSlide(
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

        SlideElement slideElement2 = elementService.addDefaultElementToSlide(
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
        assertThat(slideElement1.getX()).isEqualTo(10);
        assertThat(slideElement1.getY()).isEqualTo(10);
        assertThat(slideElement1.getAngle()).isEqualTo(10.0);
        assertThat(slideElement1.getWidth()).isEqualTo(1000L);
        assertThat(slideElement1.getHeight()).isEqualTo(1000L);

        assertThat(slideElement2.getX()).isEqualTo(20);
        assertThat(slideElement2.getY()).isEqualTo(20);
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
//        Slide testSlide = designService.newSlide(testUser, testWorkspace, testDesign, 0);
        Slide testSlide = Slide.builder().order(0).design(testDesign).build();

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
        em.persist(testSlide);

        em.flush();
        em.clear();

        //when
        SlideElement updatedSlideElement = elementService.updateSlideElement(em.find(User.class, testUser.getId()), em.find(SlideElement.class, se.getId()), em.find(Design.class, testDesign.getId()),
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

    }
}