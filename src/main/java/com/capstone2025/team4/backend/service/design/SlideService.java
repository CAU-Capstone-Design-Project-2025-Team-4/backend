package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.exception.design.DesignNotShared;
import com.capstone2025.team4.backend.exception.slide.SlideNotFound;
import com.capstone2025.team4.backend.exception.user.UserNotAllowedDesign;
import com.capstone2025.team4.backend.repository.SlideRepository;
import com.capstone2025.team4.backend.repository.element.ElementRepository;
import com.capstone2025.team4.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.capstone2025.team4.backend.service.design.DesignUtil.checkUWDS;

@Service
@RequiredArgsConstructor
@Transactional
public class SlideService {
    private final SlideRepository slideRepository;
    private final UserService userService;
    private final DesignService designService;
    private final ElementRepository elementRepository;

    public Slide newSlide(Long userId, Long designId, Integer order) {
        User user = userService.getUser(userId);

        Workspace workspace = designService.getWorkspace(user);

        Design design = designService.getDesign(designId);

        checkUWDS(user, workspace, design, null);

        Slide slide = Slide.builder()
                .order(order)
                .design(design)
                .build();

        return slideRepository.save(slide);
    }


    /**
     * srcSlide의 모든 요소를 destSrc에 복제
     */
    public Slide copySlide(Long destSlideId, Long srcSlideId, Long userId) {
        Optional<Slide> srcOptional = slideRepository.findWithSlideElementListById(srcSlideId);
        Optional<Slide> destOptional = slideRepository.findWithSlideElementListById(destSlideId);
        if (srcOptional.isEmpty() || destOptional.isEmpty()) {
            throw new SlideNotFound();
        }

        Slide srcSlide = srcOptional.get();
        if (!srcSlide.getDesign().getShared()) {
            throw new DesignNotShared();
        }

        Slide destSlide = destOptional.get();
        if (destSlide.getDesign().getUser().getId() != userId) {
            throw new UserNotAllowedDesign();
        }

        for (Element element : srcSlide.getSlideElementList()) {
            Element copy = element.copy(destSlide);
            elementRepository.save(copy);
        }

        return destSlide;
    }
}
