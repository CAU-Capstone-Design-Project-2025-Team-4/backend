package com.capstone2025.team4.backend.repository.design;

import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.domain.element.TextAlign;
import com.capstone2025.team4.backend.domain.element.TextBox;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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
        TextBox element = TextBox.builder()
                .x(15.0)
                .y(25.0)
                .z(35.0)
                .slide(slide)
                .width(50.0)
                .height(50.0)
                .angle(30.0)
                .text("testText")
                .size(100.0)
                .weight(5.0)
                .align(TextAlign.CENTER)
                .build();

        em.persist(element);
        slide.getSlideElementList().add(element);
        em.persist(element);

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
            for (Element e : s.getSlideElementList()) {
                TextBox tb = (TextBox) e;
                assertThat(tb.getX()).isEqualTo(15L);
                assertThat(tb.getY()).isEqualTo(25L);
                assertThat(tb.getZ()).isEqualTo(35L);
                assertThat(tb.getWidth()).isEqualTo(50L);
                assertThat(tb.getHeight()).isEqualTo(50L);
                assertThat(tb.getAngle()).isEqualTo(30.0);
                assertThat(tb.getText()).isEqualTo("testText");
                assertThat(tb.getSize()).isEqualTo(100L);
                assertThat(tb.getWeight()).isEqualTo(5L);
                assertThat(tb.getAlign()).isEqualTo(TextAlign.CENTER);
            }
        }

    }
}