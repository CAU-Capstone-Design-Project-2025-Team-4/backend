package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.element.spatial.CameraMode;
import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import com.capstone2025.team4.backend.domain.element.spatial.Frame;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.exception.frame.FrameNotFound;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class FrameServiceTest {

    @Autowired
    private FrameService frameService;

    @Autowired
    private EntityManager em;

    @Test
    void addFrame_givenSpatial_whenAddFrame_thenFrameIsSaved() {
        // given
        CameraTransform transform = CameraTransform.builder()
                .positionX(0.0).positionY(0.0).positionZ(0.0)
                .rotationX(0.0).rotationY(0.0).rotationZ(0.0)
                .build();
        Spatial spatial = Spatial.builder()
                .cameraMode(CameraMode.FREE)
                .cameraTransform(transform)
                .backgroundColor("#FFFFFF")
                .build();
        em.persist(spatial);
        em.flush();
        em.clear();

        // when
        Frame savedFrame = frameService.addFrame(spatial, "Test Frame", transform);

        // then
        assertThat(savedFrame.getId()).isNotNull();
        assertThat(savedFrame.getName()).isEqualTo("Test Frame");
        assertThat(savedFrame.getSpatial()).isEqualTo(spatial);
        assertThat(savedFrame.getCameraTransform()).usingRecursiveComparison()
                .isEqualTo(transform);
    }

    @Test
    void updateFrame_givenExistingFrame_whenUpdate_thenFrameIsUpdated() {
        // given
        CameraTransform original = CameraTransform.builder()
                .positionX(0.0).positionY(0.0).positionZ(0.0)
                .rotationX(0.0).rotationY(0.0).rotationZ(0.0)                .build();
        Spatial spatial = Spatial.builder()
                .cameraMode(CameraMode.FREE)
                .cameraTransform(original)
                .backgroundColor("#FFFFFF")
                .build();
        Frame frame = frameService.addFrame(spatial, "Old Name", original);

        em.persist(spatial);

        em.flush();
        em.clear();

        CameraTransform updatedTransform = CameraTransform.builder()
                .positionX(1.0).positionY(2.0).positionZ(3.0)
                .rotationX(10.0).rotationY(20.0).rotationZ(30.0)
                .build();

        // when
        Frame updated = frameService.updateFrame(spatial, frame.getId(), "New Name", updatedTransform);

        // then
        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getCameraTransform()).usingRecursiveComparison()
                .isEqualTo(updatedTransform);
    }

    @Test
    void deleteFrame_givenExistingFrame_whenDelete_thenFrameIsRemoved() {
        // given
        CameraTransform transform = CameraTransform.builder()
                .positionX(0.0).positionY(0.0).positionZ(0.0)
                .rotationX(0.0).rotationY(0.0).rotationZ(0.0)                .build();
        Spatial spatial = Spatial.builder()
                .cameraMode(CameraMode.FREE)
                .cameraTransform(transform)
                .backgroundColor("#FFFFFF")
                .build();
        Frame frame = frameService.addFrame(spatial, "To be deleted", transform);

        em.persist(spatial);
        em.persist(frame);
        em.flush();
        em.clear();

        // when
        frameService.deleteFrame(spatial, frame.getId());
        em.flush();
        em.clear();

        // then
        List<Frame> frames = frameService.findAllInSpatial(spatial);
        assertThat(frames).isEmpty();
    }

    @Test
    void getFrame_givenNonExistingFrame_whenGet_thenThrow() {
        // given
        Spatial spatial = Spatial.builder()
                .cameraMode(CameraMode.FREE)
                .cameraTransform(CameraTransform.builder().build())
                .backgroundColor("#000000")
                .build();
        em.persist(spatial);
        em.flush();

        // when / then
        assertThatThrownBy(() -> frameService.getFrame(spatial, 999L))
                .isInstanceOf(FrameNotFound.class);
    }

    @Test
    void findAllInSpatial_givenMultipleFrames_whenFindAll_thenReturnList() {
        // given
        CameraTransform transform = CameraTransform.builder()
                .positionX(0.0).positionY(0.0).positionZ(0.0)
                .rotationX(0.0).rotationY(0.0).rotationZ(0.0)                .build();
        Spatial spatial = Spatial.builder()
                .cameraMode(CameraMode.FREE)
                .cameraTransform(transform)
                .backgroundColor("#FFFFFF")
                .build();
        em.persist(spatial);
        em.flush();

        frameService.addFrame(spatial, "Frame1", transform);
        frameService.addFrame(spatial, "Frame2", transform);
        em.flush();
        em.clear();

        // when
        List<Frame> frames = frameService.findAllInSpatial(spatial);

        // then
        assertThat(frames).hasSize(2)
                .extracting(Frame::getName)
                .containsExactlyInAnyOrder("Frame1", "Frame2");
    }
}