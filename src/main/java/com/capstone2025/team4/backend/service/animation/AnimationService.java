package com.capstone2025.team4.backend.service.animation;

import com.capstone2025.team4.backend.domain.animation.Animation;
import com.capstone2025.team4.backend.domain.animation.Animation3d;
import com.capstone2025.team4.backend.domain.animation.AnimationType;
import com.capstone2025.team4.backend.domain.animation.AnimationTiming;
import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.exception.animation.AnimationNotFound;
import com.capstone2025.team4.backend.exception.element.ElementNotSpatial;
import com.capstone2025.team4.backend.repository.animation.AnimationRepository;
import com.capstone2025.team4.backend.service.design.ElementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnimationService {

    private final AnimationRepository animationRepository;
    private final ElementService elementService;

    public Animation addAnimationToElement(Long userId, Long elementId, AnimationType type, Integer duration, AnimationTiming timing, CameraTransform cameraTransform) {
        Element element = elementService.getElement(userId, elementId);

        Animation animation;
        if (cameraTransform == null) {
            animation = createAnimation(type, duration, timing, element);
        } else {
            animation = create3dAnimation(type, duration, timing, cameraTransform, element);
        }

        return animationRepository.save(animation);
    }

    private Animation create3dAnimation(AnimationType type, Integer duration, AnimationTiming timing, CameraTransform cameraTransform, Element element) {
        if (!(element instanceof Spatial)) {
            throw new ElementNotSpatial();
        }
        Animation animation;
        animation = Animation3d.builder()
                .element(element)
                .animationType(type)
                .duration(duration)
                .timing(timing)
                .cameraTransform(cameraTransform)
                .build();
        return animation;
    }

    private Animation createAnimation(AnimationType type, Integer duration, AnimationTiming timing, Element element) {
        Animation animation;
        animation = Animation.builder()
                .element(element)
                .animationType(type)
                .duration(duration)
                .timing(timing)
                .build();
        return animation;
    }

    public void deleteAnimationFromElement(Long animationId, Long elementId) {
        boolean exists = animationRepository.existsByIdAndElementId(animationId, elementId);
        if (!exists) {
            throw new AnimationNotFound();
        }

        animationRepository.deleteById(animationId);
    }

//    @Async
//    @Transactional(propagation = Propagation.REQUIRES_NEW) // TODO ; 나중에 필요하면 추가
    public void copyElementsAnimations(Long srcElementId, Element destElement) {
        List<Animation> srcElements = animationRepository.findAllByElementId(srcElementId);
        for (Animation srcAnimation : srcElements) {
            Animation copy = srcAnimation.copy(destElement);
            animationRepository.save(copy);
        }
    }

    public Animation getAnimation(Long animationId) {
        Optional<Animation> optionalAnimation = animationRepository.findWithElementById(animationId);
        if (optionalAnimation.isEmpty()) {
            throw new AnimationNotFound();
        }

        return optionalAnimation.get();
    }

    public List<Animation> getAnimationsInSlide(Long slideId) {
        return animationRepository.findAllInSlide(slideId);
    }
}
