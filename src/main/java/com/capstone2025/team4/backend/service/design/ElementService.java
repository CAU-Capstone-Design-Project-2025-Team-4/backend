package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.*;
import com.capstone2025.team4.backend.domain.design.element.Element;
import com.capstone2025.team4.backend.domain.design.element.FileElement;
import com.capstone2025.team4.backend.exception.element.ElementDefaultNotFound;
import com.capstone2025.team4.backend.exception.element.ElementNotDefault;
import com.capstone2025.team4.backend.exception.element.SlideElementNotFound;
import com.capstone2025.team4.backend.exception.slide.SlideNotFound;
import com.capstone2025.team4.backend.exception.user.UserNotAllowedAddElement;
import com.capstone2025.team4.backend.repository.ElementRepository;
import com.capstone2025.team4.backend.repository.SlideElementRepository;
import com.capstone2025.team4.backend.repository.SlideRepository;
import com.capstone2025.team4.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.capstone2025.team4.backend.service.design.DesignUtil.checkUWDS;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElementService {

    private final ElementRepository elementRepository;
    private final SlideElementRepository slideElementRepository;
    private final SlideRepository slideRepository;
    private final UserRepository userRepository;

    public SlideElement addUserFileElementToSlide(
            Long userId,
            Long slideId,
            String s3Url,
            Type type,
            Long x, Long y,
            Double angle,
            Long width, Long height
    ) {
        User user = getUser(userId);
        Slide slide = getSlide(slideId);
        Design design = slide.getDesign();
        Workspace workspace = design.getWorkspace();
        // 해당 유저가 해당 워크스페이스, 디자인의 소유자인지 확인
        checkUWDS(user, workspace, design, slide);

        FileElement element = FileElement.builder()
                .s3Url(s3Url)
                .type(type)
                .isDefault(false)
                .x(x)
                .y(y)
                .width(width)
                .height(height)
                .angle(angle)
                .build();

        elementRepository.save(element);

        SlideElement slideElement = SlideElement.builder()
                .slide(slide)
                .element(element)
                .x(x)
                .y(y)
                .width(width)
                .height(height)
                .angle(angle)
                .build();

        return slideElementRepository.save(slideElement);
    }

    // 기본으로 제공되는 요소를 슬라이드에 추가하는 기능
    public SlideElement addDefaultElementToSlide(
            Long userId,
            Long slideId,
            long elementId,
            Long x, Long y,
            Double angle,
            Long width, Long height
    ) {
        User user = getUser(userId);
        Slide slide = getSlide(slideId);
        Design design = slide.getDesign();
        Workspace workspace = design.getWorkspace();
        checkUWDS(user, workspace, design, slide);

        Optional<Element> byId = elementRepository.findById(elementId);
        if (byId.isEmpty()) {
            log.error("[addDefaultElementToSlide] Can not find element with id = {}", elementId);
            throw new ElementDefaultNotFound();
        }
        Element element = byId.get();

        if (!element.getIsDefault()) {
            log.error("[addDefaultElementToSlide] Element is not default!");
            throw new ElementNotDefault();
        }

        SlideElement slideElement = SlideElement.builder()
                .slide(slide)
                .element(element)
                .x(x)
                .y(y)
                .width(width)
                .height(height)
                .angle(angle)
                .build();

        return slideElementRepository.save(slideElement);
    }

    private Slide getSlide(Long slideId) {
        Optional<Slide> slideOptional = slideRepository.findById(slideId);
        if (slideOptional.isEmpty()) {
            throw new SlideNotFound();
        }
        return slideOptional.get();
    }

    private User getUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotAllowedAddElement();
        }
        return userOptional.get();
    }

    public SlideElement updateSlideElement(
            Long userId,
            Long slideElementId,
            long x, long y,
            double angle,
            long width, long height
    ) {
        User user = getUser(userId);
        Optional<SlideElement> slideElementOptional = slideElementRepository.findById(slideElementId);
        if (slideElementOptional.isEmpty()) {
            throw new SlideElementNotFound();
        }
        SlideElement slideElement = slideElementOptional.get();
        Design design = slideElement.getSlide().getDesign();
        checkUWDS(user, design.getWorkspace(), design, slideElement.getSlide());

        return slideElement.update(x, y, width, height, angle);
    }
}
