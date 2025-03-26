package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.*;
import com.capstone2025.team4.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DesignService {
    private final DesignRepository designRepository;
    private final WorkspaceRepository workspaceRepository;
    private final SlideRepository slideRepository;
    private final ElementRepository elementRepository;
    private final SlideElementRepository slideElementRepository;

    // 디자인을 만들때, 공유된걸 가지고 만든다면 공유 불가
    public Design createNewDesign(User creator, Design source, boolean shared) {

        Optional<Workspace> optionalWorkspace = workspaceRepository.findByUser(creator);
        if (optionalWorkspace.isEmpty()) {
            log.error("[ERROR CREATING DESIGN] No workspace for user = {}, id = {} ", creator.getEmail(), creator.getId());
            return null;
        }
        Workspace workspace = optionalWorkspace.get();
        log.debug("[CREATING NEW DESIGN] Workspace id = {}, user = {}", workspace.getId(), creator.getEmail());

        if (source != null) {
            log.debug("[CREATING NEW DESIGN] From Source(id = {})", source.getId());
            Design newDesignFromSource = createNewDesignFromSource(creator, workspace, source);
            designRepository.save(newDesignFromSource);
            log.debug("[CREATING NEW DESIGN] From Source(id = {}) Success!", source.getId());
            return newDesignFromSource;
        }

        return newDesignScratch(creator, workspace, shared);
    }

    private Design newDesignScratch(User creator, Workspace workspace, Boolean shared) {

        log.debug("[CREATING NEW DESIGN] Success!");
        Design newDesign = Design.builder()
                .user(creator)
                .workspace(workspace)
                .shared(shared)
                .build();
        return designRepository.save(newDesign);
    }

    private Design createNewDesignFromSource(User creator, Workspace workspace, Design source) {

        List<Slide> newSlideList = new ArrayList<>();

        Design newDesign = Design.builder()
                .user(creator)
                .workspace(workspace)
                .source(source)
                .slideList(newSlideList)
                .shared(false)
                .build();

        List<Slide> sourceSlideList = source.getSlideList();

        for (Slide sourceSlide : sourceSlideList) {
            ArrayList<SlideElement> newSlideElementList = new ArrayList<>();
            Slide newSlide = Slide.builder()
                    .order(sourceSlide.getOrder())
                    .design(newDesign)
                    .slideElementList(newSlideElementList)
                    .build();
            for (SlideElement slideElement : sourceSlide.getSlideElementList()) {
                SlideElement newSlideElement = SlideElement.builder()
                        .slide(newSlide)
                        .element(slideElement.getElement())
                        .x(slideElement.getX())
                        .y(slideElement.getY())
                        .width(slideElement.getWidth())
                        .height(slideElement.getHeight())
                        .angle(slideElement.getAngle())
                        .build();
                newSlideElementList.add(newSlideElement);
            }
            newSlideList.add(newSlide);
        }

        return newDesign;
    }

    public Slide newSlide(User user, Workspace workspace, Design design, Integer order) {
        // 1. 해당 유저가 해당 워크스페이스의 주인인지 확인
        if (workspace.getUser() != user) {
            return null;
        }

        // 2. 디자인이 해당 워크스페이스의 디자인인지 확인
        if (design.getWorkspace() != workspace) {
            return null;
        }

        // 3. 새로운 슬라이드 생성
        Slide slide = Slide.builder()
                .order(order)
                .design(design)
                .build();

        return slideRepository.save(slide);
    }

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
        if (UWDSareNull(user, workspace, design, slide)) {
            log.error("[addDefaultElementToSlide]Something is null. " +
                    "User = {}, " +
                    "Workspace = {}, " +
                    "Design = {}," +
                    "SLide = {}", user, workspace, design, slide);
            return null;
        }

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

        if (UWDSareNull(user, workspace, design, slide)) {
            log.error("[addDefaultElementToSlide]Something is null. " +
                    "User = {}, " +
                    "Workspace = {}, " +
                    "Design = {}," +
                    "SLide = {}", user, workspace, design, slide);
            return null;
        }

        Optional<Element> byId = elementRepository.findById(elementId);
        if (byId.isEmpty()) {
            log.error("[addDefaultElementToSlide] Can not find element with id = {}", elementId);
            return null;
        }
        Element element = byId.get();

        if (!element.getIsDefault()) {
            log.error("[addDefaultElementToSlide] Element is not default!");
            return null;
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

    private boolean UWDSareNull(User user, Workspace workspace, Design design, Slide slide) {
        if (workspace.getUser() != user) {
            return true;
        }

        if (design.getWorkspace().getUser() != user) {
            return true;
        }

        // 해당 슬라이드가 이 디자인의 슬라이드인지 확인
        if (slide.getDesign() != design) {
            return true;
        }

        return false;
    }

    public SlideElement updateSlideElement(
            User user,
            SlideElement slideElement,
            Design design,
            long x, long y,
            double angle,
            long width, long height
    ) {
        if (design.getUser() != user) {
            return null;
        }

        if (slideElement.getSlide().getDesign() != design) {
            return null;
        }

        return slideElement.update(x, y, width, height, angle);
    }
}
