package com.capstone2025.team4.backend.repository.design;

import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.design.SlideElement;
import com.capstone2025.team4.backend.domain.design.Type;
import com.capstone2025.team4.backend.domain.design.element.Element;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DesignRepositoryTest {
    @Autowired
    DesignRepository designRepository;
    @Autowired
    EntityManager em;

    @Test
    void findLongDesign(){
        //given
        // Create a Design with an empty slide list, null workspace and user, not shared, no source
        Design design = Design.builder()
                .workspace(null)
                .user(null)
                .shared(false)
                .source(null)
                .slideList(new ArrayList<>())
                .build();
        em.persist(design);

        // Create a Slide and add it to the Design
        Slide slide = Slide.builder()
                .order(1)
                .design(design)
                .slideElementList(new ArrayList<>())
                .build();
        design.getSlideList().add(slide);
        em.persist(slide);

        // Create an Element. Assuming the Type enum has a value DEFAULT.
        Element element = Element.builder()
                .type(Type.MODEL)
                .isDefault(true)
                .x(10L)
                .y(10L)
                .width(100L)
                .height(100L)
                .angle(0.0)
                .build();
        em.persist(element);

        // Create a SlideElement, associate it with the Slide and Element, and add it to the Slide
        SlideElement slideElement = SlideElement.builder()
                .slide(slide)
                .element(element)
                .x(15L)
                .y(25L)
                .width(50L)
                .height(50L)
                .angle(30.0)
                .build();
        slide.getSlideElementList().add(slideElement);
        em.persist(slideElement);

        // Flush and clear to ensure entities are written and fetched from DB
        em.flush();
        em.clear();

        //when
        Optional<Design> foundDesignOptional = designRepository.findLongDesign(design.getId());

        //then
        assertThat(foundDesignOptional.isPresent()).isTrue();
        Design foundDesign = foundDesignOptional.get();

        assertThat(foundDesign.getSlideList()).isNotNull();
        assertThat(foundDesign.getSlideList().isEmpty()).isFalse();
        for (Slide s : foundDesign.getSlideList()) {
            assertThat(s.getSlideElementList()).isNotNull();
            assertThat(s.getSlideElementList().isEmpty()).isFalse();
            for (SlideElement se : s.getSlideElementList()) {
                assertThat(se.getElement()).isNotNull();
                assertThat(se.getType()).isEqualTo(Type.MODEL);
            }
        }

    }
}