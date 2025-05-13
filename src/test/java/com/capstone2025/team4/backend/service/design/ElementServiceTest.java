package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.*;
import com.capstone2025.team4.backend.domain.element.*;
import com.capstone2025.team4.backend.domain.element.border.BorderRef;
import com.capstone2025.team4.backend.domain.element.border.BorderType;
import com.capstone2025.team4.backend.domain.element.spatial.CameraMode;
import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import com.capstone2025.team4.backend.domain.element.spatial.Model;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ElementServiceTest {

    @Autowired
    ElementService elementService;
    @Autowired
    EntityManager em;

    @Test
    void addTextBoxElementToSlide() {
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();
        Slide testSlide = Slide.builder()
                .order(0)
                .design(testDesign).build();
        BorderRef borderRef = BorderRef.builder()
                .borderType(BorderType.NONE)
                .thickness(10L)
                .color("red").build();

        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(testSlide);
        em.persist(testSlide);

        em.flush();
        em.clear();

        //when
        TextBox textBox = elementService.addTextBoxElementToSlide(testUser.getId(), testSlide.getId(), borderRef, 0L, 0L, 0L, 0.0, 10L, 10L, "testText", 100L, 100L, TextAlign.JUSTIFY, "tempFontFamily");

        //then
        assertThat(textBox).isNotNull();
        assertThat(textBox.getId()).isNotNull();
        assertThat(textBox.getText()).isEqualTo("testText");
        assertThat(textBox.getBorderRef()).isNotNull();
        assertThat(textBox.getBorderRef().getBorderType()).isEqualTo(BorderType.NONE);
        assertThat(textBox.getBorderRef().getThickness()).isEqualTo(10L);
        assertThat(textBox.getBorderRef().getColor()).isEqualTo("red");
        assertThat(textBox.getAlign()).isEqualTo(TextAlign.JUSTIFY);
        assertThat(textBox.getFontFamily()).isEqualTo("tempFontFamily");
    }

    @Test
    void updateTextBox() {
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();
        Slide testSlide = Slide.builder().order(0).design(testDesign).build();

        TextBox e = TextBox.builder()
                .slide(testSlide)
                .x(0L)
                .y(0L)
                .width(0L)
                .height(0L)
                .angle(0.0)
                .text("test").build();


        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(e);
        em.persist(testSlide);

        em.flush();
        em.clear();

        //when
        TextBox textBox = elementService.updateTextBox(testUser.getId(), e.getId(), "updated", 10L, 10L, "tempFontFamily", TextAlign.JUSTIFY);

        // then
        assertThat(textBox).isNotNull();
        assertThat(textBox.getId()).isEqualTo(e.getId());
        assertThat(textBox.getText()).isEqualTo("updated");
        assertThat(textBox.getSize()).isEqualTo(10);
        assertThat(textBox.getWeight()).isEqualTo(10);
        assertThat(textBox.getFontFamily()).isEqualTo("tempFontFamily");
        assertThat(textBox.getAlign()).isEqualTo(TextAlign.JUSTIFY);
    }

    @Test
    void addShapeElementToSlide() {
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();
        Slide testSlide = Slide.builder()
                .order(0)
                .design(testDesign).build();
        BorderRef borderRef = BorderRef.builder()
                .borderType(BorderType.NONE)
                .thickness(10L)
                .color("red").build();

        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(testSlide);
        em.persist(testSlide);

        em.flush();
        em.clear();

        //when
        Shape shape = elementService.addShapeElementToSlide(testUser.getId(), testSlide.getId(), borderRef, 10L, 10L, 10L, 0.0, 10L, 10L, "testPath", "testColor");

        //then
        assertThat(shape).isNotNull();
        assertThat(shape.getId()).isNotNull();
        assertThat(shape.getPath()).isEqualTo("testPath");
        assertThat(shape.getColor()).isEqualTo("testColor");
        assertThat(shape.getBorderRef()).isNotNull();
        assertThat(shape.getBorderRef().getBorderType()).isEqualTo(BorderType.NONE);
        assertThat(shape.getBorderRef().getThickness()).isEqualTo(10L);
        assertThat(shape.getBorderRef().getColor()).isEqualTo("red");
    }

    @Test
    void updateShape() {
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();
        Slide testSlide = Slide.builder().order(0).design(testDesign).build();

        Shape e = Shape.builder()
                .slide(testSlide)
                .x(0L)
                .y(0L)
                .width(0L)
                .height(0L)
                .angle(0.0)
                .path("testPath")
                .color("testColor")
                .build();


        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(e);
        em.persist(testSlide);

        em.flush();
        em.clear();

        //when
        Shape shape = elementService.updateShape(testUser.getId(), e.getId(), "updatedPath", "updatedColor");

        // then
        assertThat(shape).isNotNull();
        assertThat(shape.getId()).isEqualTo(e.getId());
        assertThat(shape.getPath()).isEqualTo("updatedPath");
        assertThat(shape.getColor()).isEqualTo("updatedColor");
    }

    @Test
    void addImageElementToSlide() {
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();
        Slide testSlide = Slide.builder()
                .order(0)
                .design(testDesign).build();
        BorderRef borderRef = BorderRef.builder()
                .borderType(BorderType.NONE)
                .thickness(10L)
                .color("red").build();

        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(testSlide);
        em.persist(testSlide);

        em.flush();
        em.clear();

        //when
        Image image = elementService.addImageElementToSlide(testUser.getId(), testSlide.getId(), borderRef, 10L, 10L, 10L, 0.0, 10L, 10L, "s3Url");

        //then
        assertThat(image).isNotNull();
        assertThat(image.getId()).isNotNull();
        assertThat(image.getBorderRef()).isNotNull();
        assertThat(image.getBorderRef().getBorderType()).isEqualTo(BorderType.NONE);
        assertThat(image.getBorderRef().getThickness()).isEqualTo(10L);
        assertThat(image.getBorderRef().getColor()).isEqualTo("red");
        assertThat(image.getUrl()).isEqualTo("s3Url");
    }

    @Test
    void addSpatialElementToSlide() {
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();
        Slide testSlide = Slide.builder()
                .order(0)
                .design(testDesign).build();
        BorderRef borderRef = BorderRef.builder()
                .borderType(BorderType.NONE)
                .thickness(10L)
                .color("red").build();
        CameraTransform cameraTransform = CameraTransform.builder()
                .positionX(0L)
                .positionY(0L)
                .positionZ(0L)
                .rotationX(0L)
                .rotationY(0L)
                .rotationZ(0L).build();

        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(testSlide);
        em.persist(testSlide);

        em.flush();
        em.clear();

        //when
        Spatial spatial = elementService.addSpatialElementToSlide(
                testUser.getId(),
                testSlide.getId(),
                borderRef,
                10L, 10L, 10L, 0.0, 100L, 200L,
                CameraMode.FREE,
                cameraTransform,
                "3D content",
                "#ffffff"
        );

        //then
        assertThat(spatial).isNotNull();
        assertThat(spatial.getId()).isNotNull();
        assertThat(spatial.getBorderRef()).isNotNull();
        assertThat(spatial.getBorderRef().getBorderType()).isEqualTo(BorderType.NONE);
        assertThat(spatial.getCameraMode()).isEqualTo(CameraMode.FREE);
        assertThat(spatial.getCameraTransform()).isNotNull();
        assertThat(spatial.getBackgroundColor()).isEqualTo("#ffffff");
    }

    @Test
    void updateSpatial() {
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();
        Slide testSlide = Slide.builder().order(0).design(testDesign).build();
        CameraTransform cameraTransform = CameraTransform.builder()
                .positionX(0L)
                .positionY(0L)
                .positionZ(0L)
                .rotationX(0L)
                .rotationY(0L)
                .rotationZ(0L).build();

        CameraTransform cameraTransformUpdate = CameraTransform.builder()
                .positionX(10L)
                .positionY(10L)
                .positionZ(10L)
                .rotationX(10L)
                .rotationY(10L)
                .rotationZ(10L).build();

        Spatial e = Spatial.builder()
                .slide(testSlide)
                .x(0L)
                .y(0L)
                .width(0L)
                .height(0L)
                .angle(0.0)
                .cameraTransform(cameraTransform)
                .cameraMode(CameraMode.FREE)
                .backgroundColor("testColor")
                .build();


        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(e);
        em.persist(testSlide);

        em.flush();
        em.clear();

        //when
        Spatial spatial = elementService.updateSpatial(testUser.getId(), e.getId(), CameraMode.ORBIT, cameraTransformUpdate, "updateColor");

        // then
        assertThat(spatial).isNotNull();
        assertThat(spatial.getId()).isEqualTo(e.getId());
        assertThat(spatial.getCameraMode()).isEqualTo(CameraMode.ORBIT);
        assertThat(spatial.getCameraTransform()).isNotNull();
        assertThat(spatial.getCameraTransform().getPositionX()).isEqualTo(10L);
        assertThat(spatial.getCameraTransform().getPositionY()).isEqualTo(10L);
        assertThat(spatial.getCameraTransform().getPositionZ()).isEqualTo(10L);
        assertThat(spatial.getCameraTransform().getRotationX()).isEqualTo(10L);
        assertThat(spatial.getCameraTransform().getRotationY()).isEqualTo(10L);
        assertThat(spatial.getCameraTransform().getRotationZ()).isEqualTo(10L);
        assertThat(spatial.getBackgroundColor()).isEqualTo("updateColor");
    }

    @Test
    void updateCommonFields() {
        //given
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();
        Slide testSlide = Slide.builder().order(0).design(testDesign).build();

        TextBox e = TextBox.builder()
                .slide(testSlide)
                .x(0L)
                .y(0L)
                .width(0L)
                .height(0L)
                .angle(0.0)
                .text("test").build();


        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(testSlide);
        em.persist(e);

        em.flush();
        em.clear();

        //when
        Element element = elementService.updateCommonFields(testUser.getId(), e.getId(), null, 10L, 10L, 10L, 10L, 10L, 10.0);

        // then
        assertThat(element).isNotNull();
        assertThat(element.getX()).isEqualTo(10L);
        assertThat(element.getY()).isEqualTo(10L);
        assertThat(element.getZ()).isEqualTo(10L);
        assertThat(element.getWidth()).isEqualTo(10L);
        assertThat(element.getHeight()).isEqualTo(10L);
        assertThat(element.getAngle()).isEqualTo(10.0);
    }

    @Test
    void getAllElementsInSlide(){
        //given
        String TEST_URL = "testUrl";
        User testUser = User.builder()
                .email("test@example.com")
                .build();
        Workspace testWorkspace = new Workspace(testUser);
        Design testDesign = Design.builder()
                .user(testUser)
                .workspace(testWorkspace)
                .build();
        Slide testSlide = Slide.builder().order(0).design(testDesign).build();

        TextBox textBox = TextBox.builder()
                .slide(testSlide)
                .x(0L).y(0L).z(0L)
                .width(100L).height(100L)
                .angle(0.0)
                .text("SampleText")
                .build();

        Shape shape = Shape.builder()
                .slide(testSlide)
                .x(10L).y(10L).z(0L)
                .width(50L).height(50L)
                .angle(0.0)
                .path("shapePath")
                .color("blue")
                .build();

        Image image = Image.builder()
                .slide(testSlide)
                .x(20L).y(20L).z(0L)
                .width(80L).height(80L)
                .angle(0.0)
                .url("imageUrl")
                .build();

        CameraTransform cameraTransform = CameraTransform.builder()
                .positionX(0L).positionY(0L).positionZ(0L)
                .rotationX(0L).rotationY(0L).rotationZ(0L)
                .build();

        Model model = Model.builder().url(TEST_URL).build();
        Spatial spatial = Spatial.builder()
                .slide(testSlide)
                .x(30L).y(30L).z(0L)
                .width(120L).height(120L)
                .angle(0.0)
                .cameraMode(CameraMode.FREE)
                .cameraTransform(cameraTransform)
                .backgroundColor("#ffffff")
                .build();
        spatial.addModel(model);

        em.persist(testUser);
        em.persist(testWorkspace);
        em.persist(testDesign);
        em.persist(testSlide);
        em.persist(textBox);
        em.persist(shape);
        em.persist(image);
        em.persist(spatial);
        em.persist(model);

        em.flush();
        em.clear();

        //when
        System.out.println("----");
        List<Element> allElementsInSlide = elementService.findAllElementsInSlide(testUser.getId(), testSlide.getId());
        System.out.println("----");

        //then
        assertThat(allElementsInSlide).hasSize(4);
        assertThat(allElementsInSlide).extracting("class").containsExactlyInAnyOrder(
                TextBox.class,
                Shape.class,
                Image.class,
                Spatial.class
        );
        assertThat(allElementsInSlide).filteredOn(e -> e instanceof TextBox)
                .extracting(e -> ((TextBox) e).getText()).containsExactly("SampleText");

        assertThat(allElementsInSlide).filteredOn(e -> e instanceof Shape)
                .extracting(e -> ((Shape) e).getPath()).containsExactly("shapePath");

        assertThat(allElementsInSlide).filteredOn(e -> e instanceof Image)
                .extracting(e -> ((Image) e).getUrl()).containsExactly("imageUrl");

        List<Model> modelsInSlide = allElementsInSlide.stream()
                .filter(e -> e instanceof Spatial)
                .map(e -> (Spatial) e)
                .findFirst()
                .orElseThrow()
                .getModels();

        assertThat(modelsInSlide)
                .hasSize(1)
                .allSatisfy(m -> {
                    assertThat(m.getUrl()).isEqualTo(TEST_URL);
                });
    }
}