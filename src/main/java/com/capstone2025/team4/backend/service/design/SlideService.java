package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.exception.file.FileIsEmpty;
import com.capstone2025.team4.backend.exception.slide.SlideNotFound;
import com.capstone2025.team4.backend.exception.slide.SlideOrderDuplicate;
import com.capstone2025.team4.backend.exception.user.UserNotAllowed;
import com.capstone2025.team4.backend.repository.slide.SlideRepository;
import com.capstone2025.team4.backend.repository.element.ElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SlideService {
    private final SlideRepository slideRepository;
    private final ElementRepository elementRepository;

    public Slide newSlide(User user, Design design, Workspace workspace, int order) {
        if (design.getUser() != user && design.getWorkspace() != workspace) {
            throw new UserNotAllowed();
        }

        boolean duplicateExists = slideRepository.existsByDesignIdAndOrder(design.getId(), order);
        if (duplicateExists) {
            throw new SlideOrderDuplicate();
        }

        Slide slide = Slide.builder()
                .order(order)
                .design(design)
                .build();

        return slideRepository.save(slide);
    }

    protected void addInitSlide(Design design) {
        Slide slide = Slide.builder()
                .design(design)
                .order(1)
                .build();

        design.getSlideList().add(slide);

        slideRepository.save(slide);
    }


    /**
     * srcSlide의 모든 요소를 destSrc에 복제
     */
    public Slide copySlide(Long destSlideId, Long srcSlideId, Long userId) {
        Optional<Slide> srcOptional = slideRepository.findSlideWithElementsSharedDesign(srcSlideId);
        Optional<Slide> destOptional = slideRepository.findSlideWithElements(userId, destSlideId);
        if (srcOptional.isEmpty() || destOptional.isEmpty()) {
            throw new SlideNotFound();
        }

        Slide srcSlide = srcOptional.get();
        Slide destSlide = destOptional.get();

        for (Element element : srcSlide.getSlideElementList()) {
            Element copy = element.copy(destSlide);
            elementRepository.save(copy);
        }

        destSlide.changeThumbnail(srcSlide.getThumbnail());

        return destSlide;
    }

    public List<Slide> findAllInDesign(Long userId, Long designId) {
        return slideRepository.findAllByDesignIdAndUserId(designId, userId);
    }

    public void delete(Long userId, Long slideId) {
        Optional<Slide> optionalSlide = slideRepository.findWithDesign(userId, slideId);

        if (optionalSlide.isEmpty()) {
            throw new SlideNotFound();
        }
        Slide slide = optionalSlide.get();
        int deletedOrder = slide.getOrder();
        Long designId = slide.getDesign().getId();

        slideRepository.deleteById(slideId);
        slideRepository.flush();

        slideRepository.reorderSlidesAfterDeletion(designId, deletedOrder);
    }

    public Slide changeThumbnail(Long userId, Long designId, Long slideId, byte[] image) {
        Optional<Slide> optionalSlide = slideRepository.findByIdAndDesignIdAndUserId(slideId, designId, userId);
        if (optionalSlide.isEmpty()) {
            throw new SlideNotFound();
        }

        Slide slide = optionalSlide.get();

        if (image.length == 0) {
            throw new FileIsEmpty();
        }

        slide.changeThumbnail(image);
        return slide;
    }

    public Slide getSlide(Long userId, Long slideId) {
        Optional<Slide> optionalSlide = slideRepository.findSlide(userId, slideId);
        if (optionalSlide.isEmpty()) {
            throw new SlideNotFound();
        }

        return optionalSlide.get();
    }

    public Slide getSlideWithElements(Long userId, Long slideId) {
        Optional<Slide> optionalSlide = slideRepository.findSlideWithElements(userId, slideId);
        if (optionalSlide.isEmpty()) {
            throw new SlideNotFound();
        }

        return optionalSlide.get();
    }

    public Slide createNewSlide(Slide sourceSlide, Design newDesign, ArrayList<Element> newSlideElementList) {
        Slide newSlide = Slide.builder()
                .order(sourceSlide.getOrder())
                .design(newDesign)
                .slideElementList(newSlideElementList)
                .thumbnail(sourceSlide.getThumbnail())
                .build();
        slideRepository.save(newSlide);
        return newSlide;
    }

}
