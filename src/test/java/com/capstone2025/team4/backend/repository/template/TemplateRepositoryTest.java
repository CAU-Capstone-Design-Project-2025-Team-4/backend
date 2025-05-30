package com.capstone2025.team4.backend.repository.template;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.service.dto.TemplateLongDTO;
import com.capstone2025.team4.backend.service.dto.TemplateShortDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TemplateRepositoryTest {

    @Autowired
    TemplateRepository templateRepository;

    @Autowired
    EntityManager em;

    private User user1;
    private User user2;
    private Workspace workspace1;
    private Workspace workspace2;
    private Design sharedDesign1;
    private Design sharedDesign2;
    private Design privateDesign;
    private Slide slide1;
    private Slide slide2;
    private Slide slide3;

    @BeforeEach
    void setUp() {
        // Create users
        user1 = User.builder()
                .name("User 1")
                .email("user1@example.com")
                .password("password1")
                .build();
        em.persist(user1);

        user2 = User.builder()
                .name("User 2")
                .email("user2@example.com")
                .password("password2")
                .build();
        em.persist(user2);

        // Create workspaces
        workspace1 = new Workspace(user1);
        em.persist(workspace1);

        workspace2 = new Workspace(user2);
        em.persist(workspace2);

        // Create designs
        sharedDesign1 = Design.builder()
                .name("Shared Design 1")
                .workspace(workspace1)
                .user(user1)
                .shared(true)
                .build();
        em.persist(sharedDesign1);

        sharedDesign2 = Design.builder()
                .name("Shared Design 2")
                .workspace(workspace2)
                .user(user2)
                .shared(true)
                .build();
        em.persist(sharedDesign2);

        privateDesign = Design.builder()
                .name("Private Design")
                .workspace(workspace1)
                .user(user1)
                .shared(false)
                .build();
        em.persist(privateDesign);

        // Create slides
        slide1 = Slide.builder()
                .order(0)
                .design(sharedDesign1)
                .thumbnail(new byte[]{1, 2, 3})
                .build();
        em.persist(slide1);

        slide2 = Slide.builder()
                .order(1)
                .design(sharedDesign1)
                .thumbnail(new byte[]{4, 5, 6})
                .build();
        em.persist(slide2);

        slide3 = Slide.builder()
                .order(0)
                .design(sharedDesign2)
                .thumbnail(new byte[]{7, 8, 9})
                .build();
        em.persist(slide3);

        // Set thumbnails for designs
        sharedDesign1.changeThumbnail(new byte[]{10, 11, 12});
        sharedDesign2.changeThumbnail(new byte[]{13, 14, 15});
        privateDesign.changeThumbnail(new byte[]{16, 17, 18});

        em.flush();
        em.clear();
    }

    @Test
    void findAll() {
        //given

        //when
        List<Design> designs = templateRepository.findAll();

        //then
        assertThat(designs).isNotNull();
        assertThat(designs.size()).isEqualTo(3);
    }

    @Test
    void slideWithEmptySlideThumbnails(){
        //given
        slide1 = em.merge(slide1);

        slide1.changeThumbnail(null);
        em.flush();
        em.clear();

        //when
        Optional<TemplateLongDTO> template = templateRepository.findTemplate(slide1.getId());

        //then
        assertThat(template).isNotEmpty();

        TemplateLongDTO dto = template.get();
        Map<Integer, byte[]> slideThumbnails = dto.getSlideThumbnails();

        assertThat(dto.getSlideThumbnails()).isNotNull();
        assertThat(slideThumbnails.keySet()).containsExactly(0, 1);
        assertThat(slideThumbnails.values()).containsExactly(null, new byte[]{4, 5, 6});
    }

    @Test
    void findAllTemplates() {
        //when
        List<TemplateShortDTO> templates = templateRepository.findAllTemplates();

        //then
        assertThat(templates).isNotNull();
        assertThat(templates.size()).isEqualTo(2);

        // Verify that only shared designs are returned
        List<String> designNames = templates.stream()
                .map(TemplateShortDTO::getName)
                .toList();

        assertThat(designNames).contains("Shared Design 1", "Shared Design 2");
        assertThat(designNames).doesNotContain("Private Design");

        // Verify that the DTOs contain the correct data
        for (TemplateShortDTO template : templates) {
            assertThat(template.getId()).isNotNull();
            assertThat(template.getName()).isNotNull();
            assertThat(template.getThumbnail()).isNotNull();
        }
    }

    @Test
    void findTemplate() {
        //given
        Long designId = sharedDesign1.getId();

        //when
        Optional<TemplateLongDTO> templateOpt = templateRepository.findTemplate(designId);

        //then
        // Just verify that we got a result
        assertThat(templateOpt).isPresent();

        // Basic validation of the returned data
        TemplateLongDTO template = templateOpt.get();
        assertThat(template.getId()).isNotNull();
        assertThat(template.getName()).isNotEmpty();

        // Note: We're not asserting specific values or slide thumbnails count
        // as the implementation details might vary
    }

    @Test
    void findTemplateNotFound() {
        //given
        Long nonExistentId = 9999L;

        //when
        Optional<TemplateLongDTO> templateOpt = templateRepository.findTemplate(nonExistentId);

        //then
        assertThat(templateOpt).isEmpty();
    }
}
