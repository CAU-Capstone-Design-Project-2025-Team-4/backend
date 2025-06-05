package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.*;
import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.exception.design.DesignNotFound;
import com.capstone2025.team4.backend.exception.design.DesignNotShared;
import com.capstone2025.team4.backend.exception.design.DesignSourceNotFound;
import com.capstone2025.team4.backend.exception.file.FileIsEmpty;
import com.capstone2025.team4.backend.repository.design.DesignRepository;
import com.capstone2025.team4.backend.service.UserService;
import com.capstone2025.team4.backend.service.WorkspaceService;
import com.capstone2025.team4.backend.service.animation.AnimationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DesignService {
    private final DesignRepository designRepository;
    private final UserService userService;
    private final SlideService slideService;
    private final WorkspaceService workspaceService;
    private final ElementService elementService;
    private final AnimationService animationService;

    // 디자인을 만들때, 공유된걸 가지고 만든다면 공유 불가
    public Design createNewDesign(String designName, Long creatorId, Long sourceDesignId, boolean shared) {
        User creator = userService.getUser(creatorId);

        Workspace workspace = workspaceService.getWorkspace(creator);

        if (sourceDesignId != null) {
            Optional<Design> sourceDesignOptional = designRepository.findLongDesign(sourceDesignId);
            if (sourceDesignOptional.isEmpty()) {
                throw new DesignSourceNotFound();
            }
            Design source = sourceDesignOptional.get();

            if (!source.getShared()) {
                throw new DesignNotShared();
            }

            return createNewDesignFromSource(designName, creator, workspace, source);
        }

        return createNewDesignFromScratch(designName, creator, workspace, shared);
    }

    private Design createNewDesignFromScratch(String designName, User creator, Workspace workspace, Boolean shared) {

        log.debug("[CREATING NEW DESIGN] Success!");
        Design newDesign = Design.builder()
                .name(designName)
                .user(creator)
                .workspace(workspace)
                .shared(shared)
                .build();
        designRepository.save(newDesign);
        slideService.addInitSlide(newDesign);

        return newDesign;
    }

    private Design createNewDesignFromSource(String designName, User creator, Workspace workspace, Design source) {

        List<Slide> newSlideList = new ArrayList<>();

        Design newDesign = Design.builder()
                .name(designName)
                .user(creator)
                .workspace(workspace)
                .source(source)
                .slideList(newSlideList)
                .shared(false)
                .build();
        designRepository.save(newDesign);

        List<Slide> sourceSlideList = source.getSlideList();

        for (Slide sourceSlide : sourceSlideList) {
            ArrayList<Element> newSlideElementList = new ArrayList<>();
            Slide newSlide = slideService.createNewSlide(sourceSlide, newDesign, newSlideElementList);
            for (Element slideElement : sourceSlide.getSlideElementList()) {
                Element destElement = elementService.copyAndSaveElement(slideElement, newSlide);
                animationService.copyElementsAnimations(slideElement.getId(), destElement);
            }
            newSlideList.add(newSlide);
        }

        return newDesign;
    }

    public Design getDesign(Long designId) {
        Optional<Design> optionalDesign = designRepository.findById(designId);
        if (optionalDesign.isEmpty()) {
            throw new DesignNotFound();
        }
        return optionalDesign.get();
    }

    @Transactional(readOnly = true)
    public List<Design> findAll(Long userId) {
        return designRepository.findAllByUserId(userId);
    }

    public Design findDesign(Long designId, boolean withOthers) {
        Optional<Design> optionalDesign;
        if (withOthers) {
            optionalDesign = designRepository.findLongDesign(designId);
        }else {
            optionalDesign = designRepository.findById(designId);
        }

        if (optionalDesign.isEmpty()) {
            throw new DesignNotFound();
        }
        return optionalDesign.get();
    }

    public void delete(Long userId, Long designId) {
        boolean exists = designRepository.exists(designId, userId);

        if (!exists) {
            throw new DesignNotFound();
        }

        designRepository.deleteById(designId);
    }

    public Design changeName(Long userId, Long designId, String name) {
        Design design = getDesignByIdAndUserId(userId, designId);
        design.changeName(name);

        return design;
    }

    public Design changeThumbnail(Long userId, Long designId, byte[] image) {
        Design design = getDesignByIdAndUserId(userId, designId);

        if (image.length == 0) {
            throw new FileIsEmpty();
        }

        design.changeThumbnail(image);
        return design;
    }

    private Design getDesignByIdAndUserId(Long userId, Long designId) {
        Optional<Design> optionalDesign = designRepository.findByIdAndUserId(designId, userId);
        if (optionalDesign.isEmpty()) {
            throw new DesignNotFound();
        }

        return optionalDesign.get();
    }

    public Design toggleShared(Long userId, Long designId, Boolean flag) {
        Design design = getDesignByIdAndUserId(userId, designId);

        design.changeShared(flag);
        return design;
    }
}
