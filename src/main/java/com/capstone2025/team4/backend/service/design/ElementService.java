package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.*;
import com.capstone2025.team4.backend.exception.element.ElementDefaultNotFound;
import com.capstone2025.team4.backend.exception.element.ElementNotDefault;
import com.capstone2025.team4.backend.repository.ElementRepository;
import com.capstone2025.team4.backend.repository.SlideElementRepository;
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

    // 유자 직접 추가한 요소를 슬라이드에 추가하는 기능
    public SlideElement addUserElementToSlide(
            User user,
            Workspace workspace,
            Design design,
            Slide slide,
            String url,
            String typeString,
            Long x, Long y,
            Double angle,
            Long width, Long height
    ) {
        // 해당 유저가 해당 워크스페이스, 디자인의 소유자인지 확인
        checkUWDS(user, workspace, design, slide);

        Type type = Type.valueOf(typeString);

        Element element = Element.builder()
                .url(url) // TODO : S3 추가
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
            User user,
            Workspace workspace,
            Design design,
            Slide slide,
            long elementId,
            Long x, Long y,
            Double angle,
            Long width, Long height
    ) {

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



    public SlideElement updateSlideElement(
            User user,
            SlideElement slideElement,
            Design design,
            long x, long y,
            double angle,
            long width, long height
    ) {
        checkUWDS(user, design.getWorkspace(), design, slideElement.getSlide());

        return slideElement.update(x, y, width, height, angle);
    }
}
