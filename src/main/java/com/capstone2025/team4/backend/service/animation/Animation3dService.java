package com.capstone2025.team4.backend.service.animation;

import com.capstone2025.team4.backend.domain.animation3d.Animation3D;
import com.capstone2025.team4.backend.domain.animation3d.Animation3dType;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.exception.animation.AnimationNotFound;
import com.capstone2025.team4.backend.repository.animation3d.Animation3dRepository;
import com.capstone2025.team4.backend.service.design.ElementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class Animation3dService {
    private final Animation3dRepository animation3dRepository;
    private final ElementService elementService;

    public Animation3D addAnimationToElement(Long userId, Long spatialId, Animation3dType type) {
        Spatial element = elementService.getSpatial(userId, spatialId);

        Animation3D animation = Animation3D.builder()
                .spatial(element)
                .type(type)
                .build();

        return animation3dRepository.save(animation);
    }

    public void deleteAnimationFromElement(Long animationId, Long spatialId) {
        boolean exists = animation3dRepository.existsByIdAndSpatialId(animationId, spatialId);
        if (!exists) {
            throw new AnimationNotFound();
        }

        animation3dRepository.deleteById(animationId);
    }

    public void copyElementsAnimations(Long srcElementId, Spatial destElement) {
        List<Animation3D> srcElements = animation3dRepository.findAllBySpatialId(srcElementId);
        for (Animation3D srcAnimation : srcElements) {
            Animation3D copy = srcAnimation.copy(destElement);
            animation3dRepository.save(copy);
        }
    }

    public Animation3D getAnimation(Long animationId) {
        Optional<Animation3D> optionalAnimation = animation3dRepository.findWithElementById(animationId);
        if (optionalAnimation.isEmpty()) {
            throw new AnimationNotFound();
        }

        return optionalAnimation.get();
    }

    public List<Animation3D> getAnimationsInSlide(Long slideId) {
        return animation3dRepository.findAllInSlide(slideId);
    }
}
