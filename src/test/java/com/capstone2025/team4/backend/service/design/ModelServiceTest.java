package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.element.model.Model;
import com.capstone2025.team4.backend.domain.element.model.ModelShader;
import com.capstone2025.team4.backend.domain.element.model.ModelTransform;
import com.capstone2025.team4.backend.domain.element.model.Vector3;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.infra.aws.S3Service;
import com.capstone2025.team4.backend.repository.model.ModelRepository;
import com.capstone2025.team4.backend.repository.element.ElementRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
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


    @Test
    @Rollback(value = false)
    void update(){
        //given
        User user = User.builder()
                .build();
        Design design = Design.builder().user(user).build();
        Slide slide = Slide.builder().design(design).build();
        Spatial spatial = Spatial.builder()
                .slide(slide)
                .build();
        Vector3 positionVectorInit = Vector3.builder().x(1).y(2).z(3).build();
        Vector3 rotationVectorInit = Vector3.builder().x(0).y(90).z(0).build();
        Vector3 scaleVectorInit = Vector3.builder().x(1).y(1).z(1).build();
        Model model = Model.builder()
                .shader(ModelShader.HIGHLIGHT)
                .name("initName")
                .modelTransform(ModelTransform.builder()
                        .position(positionVectorInit)
                        .rotation(rotationVectorInit)
                        .scale(scaleVectorInit)
                        .build())
                .build();
        model.setSpatial(spatial);

        em.persist(user);
        em.persist(design);
        em.persist(slide);
        em.persist(spatial);
        em.persist(model);

        em.flush();
        em.clear();

        //when
        Model updatedModel = modelService.update(
                model.getId(),
                spatial.getId(),
                user.getId(),
                "UpdatedName",
                ModelShader.NONE,
                ModelTransform.builder()
                        .position(Vector3.builder().x(10).y(20).z(30).build())
                        .rotation(Vector3.builder().x(45).y(90).z(0).build())
                        .scale(Vector3.builder().x(2).y(2).z(2).build())
                        .build()
        );

        //then
        assertEquals(ModelShader.NONE, updatedModel.getShader());
        assertEquals("UpdatedName", updatedModel.getName());
        assertEquals(10, updatedModel.getModelTransform().getPosition().getX());
        assertEquals(90, updatedModel.getModelTransform().getRotation().getY());
        assertEquals(2, updatedModel.getModelTransform().getScale().getX());
    }
}