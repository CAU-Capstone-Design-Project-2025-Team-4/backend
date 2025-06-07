package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import com.capstone2025.team4.backend.domain.element.spatial.Frame;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.exception.frame.FrameNotFound;
import com.capstone2025.team4.backend.repository.frame.FrameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FrameService {

    private final FrameRepository frameRepository;

    public Frame addFrame(Spatial spatial, String name, CameraTransform cameraTransform) {
        Frame frame = Frame.builder()
                .name(name)
                .cameraTransform(cameraTransform)
                .spatial(spatial)
                .build();

        return frameRepository.save(frame);
    }

    public void deleteFrame(Spatial spatial, Long frameId) {
        frameRepository.deleteByIdAndSpatial(frameId, spatial);
    }

    public Frame updateFrame(Spatial spatial, Long frameId, String name, CameraTransform cameraTransform) {
        Frame frame = getFrame(spatial, frameId);
        frame.update(cameraTransform, name);
        return frame;
    }

    public Frame getFrame(Spatial spatial, Long frameId) {
        Optional<Frame> optionalFrame = frameRepository.findByIdAndSpatial(frameId, spatial);
        if (optionalFrame.isEmpty()) {
            throw new FrameNotFound();
        }
        return optionalFrame.get();
    }

    public List<Frame> findAllInSpatial(Spatial spatial) {
        return frameRepository.findAllBySpatial(spatial);
    }
}
