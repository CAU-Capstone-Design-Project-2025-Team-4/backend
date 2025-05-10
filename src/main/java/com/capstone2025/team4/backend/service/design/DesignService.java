package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.*;
import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.exception.design.DesignNotFound;
import com.capstone2025.team4.backend.exception.design.DesignNotShared;
import com.capstone2025.team4.backend.exception.design.DesignSourceNotFound;
import com.capstone2025.team4.backend.repository.*;
import com.capstone2025.team4.backend.repository.design.DesignRepository;
import com.capstone2025.team4.backend.service.UserService;
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
    private final WorkspaceRepository workspaceRepository;
    private final UserService userService;
    private final SlideService slideService;

    // 디자인을 만들때, 공유된걸 가지고 만든다면 공유 불가
    public Design createNewDesign(Long creatorId, Long sourceDesignId, boolean shared) {
        User creator = userService.getUser(creatorId);

        Workspace workspace = getWorkspace(creator);
        log.debug("[CREATING NEW DESIGN] Workspace id = {}, user = {}", workspace.getId(), creator.getEmail());

        if (sourceDesignId != null) {
            Optional<Design> sourceDesignOptional = designRepository.findById(sourceDesignId);
            if (sourceDesignOptional.isEmpty()) {
                throw new DesignSourceNotFound();
            }
            Design source = sourceDesignOptional.get();

            if (!source.getShared()) {
                throw new DesignNotShared();
            }

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
        designRepository.save(newDesign);
        slideService.addInitSlide(newDesign);

        return newDesign;
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
            ArrayList<Element> newSlideElementList = new ArrayList<>();
            Slide newSlide = Slide.builder()
                    .order(sourceSlide.getOrder())
                    .design(newDesign)
                    .slideElementList(newSlideElementList)
                    .build();
            for (Element slideElement : sourceSlide.getSlideElementList()) {
                slideElement.copy(newSlide);
            }
            newSlideList.add(newSlide);
        }

        return newDesign;
    }


    public Workspace getWorkspace(User creator) {
        Optional<Workspace> optionalWorkspace = workspaceRepository.findByUser(creator);
        if (optionalWorkspace.isEmpty()) {
            log.error("No workspace for user = {}, id = {} ", creator.getEmail(), creator.getId());
            throw new RuntimeException("워크스페이스 없는 사용자입니다.");
        }
        return optionalWorkspace.get();
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

    public Design findDesign(Long designId) {
        Optional<Design> optionalDesign = designRepository.findById(designId);
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
}
