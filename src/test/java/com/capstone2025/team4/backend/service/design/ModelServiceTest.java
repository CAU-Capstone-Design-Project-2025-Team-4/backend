package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.element.model.Model;
import com.capstone2025.team4.backend.domain.element.model.ModelShader;
import com.capstone2025.team4.backend.domain.element.model.ModelTransform;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.infra.aws.S3Service;
import com.capstone2025.team4.backend.repository.ModelRepository;
import com.capstone2025.team4.backend.repository.element.ElementRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
class ModelServiceTest {

    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private ElementRepository elementRepository;

    @MockitoBean
    private S3Service s3Service;

    @Autowired
    EntityManager em;

    ModelService modelService;


    @BeforeEach
    public void beforeEach() {
        modelService = new ModelService(modelRepository, s3Service, elementRepository);
    }

    @Test
    void addModel(){
        //given
        User user = User.builder()
                .build();
        Design design = Design.builder().user(user).build();
        Slide slide = Slide.builder().design(design).build();
        Spatial spatial = Spatial.builder()
                .slide(slide)
                .build();
        String fileName = "fileName";
        MultipartFile file = new MockMultipartFile(fileName, "file".getBytes(StandardCharsets.UTF_8));
        ModelShader modelShader = ModelShader.NONE;
        ModelTransform modelTransform = ModelTransform.builder().build();
        given(s3Service.upload(file)).willReturn("tempUrl");


        em.persist(user);
        em.persist(design);
        em.persist(slide);
        em.persist(spatial);

        em.flush();
        em.clear();

        //when
        Model model = modelService.addModel(spatial.getId(), user.getId(), file, fileName, modelShader, modelTransform);

        //then
        assertNotNull(model.getId());
        assertEquals(fileName, model.getName());
        assertEquals("tempUrl", model.getUrl());
        assertEquals(modelShader, model.getShader());
        assertEquals(modelTransform, model.getModelTransform());
        assertEquals(spatial.getId(), model.getSpatial().getId());
    }
}