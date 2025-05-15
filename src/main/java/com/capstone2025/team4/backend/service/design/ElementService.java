package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.*;
import com.capstone2025.team4.backend.domain.element.*;
import com.capstone2025.team4.backend.domain.element.border.BorderRef;
import com.capstone2025.team4.backend.domain.element.spatial.CameraMode;
import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import com.capstone2025.team4.backend.domain.element.model.Model;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.exception.element.ElementNotFound;
import com.capstone2025.team4.backend.exception.slide.SlideNotFound;
import com.capstone2025.team4.backend.infra.aws.S3Service;
import com.capstone2025.team4.backend.repository.element.*;
import com.capstone2025.team4.backend.repository.SlideRepository;
import com.capstone2025.team4.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.capstone2025.team4.backend.service.design.DesignUtil.checkUWDS;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ElementService {

    private final ElementRepository elementRepository;
    private final SlideRepository slideRepository;
    private final UserService userService;
    private final ModelService modelService;
    private final S3Service s3Service;

    public TextBox addTextBoxElementToSlide(
            Long userId,
            Long slideId,
            BorderRef borderRef,
            Long x, Long y, Long z,
            Double angle,
            Long width, Long height,
            String text,
            Long size,
            Long weight,
            TextAlign align,
            String fontFamily
    ) {
        User user = userService.getUser(userId);
        Slide slide = getSlide(slideId, false);
        Design design = slide.getDesign();
        Workspace workspace = design.getWorkspace();
        // 해당 유저가 해당 워크스페이스, 디자인의 소유자인지 확인
        checkUWDS(user, workspace, design, slide);

        TextBox element = TextBox.builder()
                .slide(slide)
                .borderRef(borderRef)
                .x(x)
                .y(y)
                .z(z)
                .angle(angle)
                .width(width)
                .height(height)
                .text(text)
                .size(size)
                .weight(weight)
                .align(align)
                .fontFamily(fontFamily)
                .build();

        return elementRepository.save(element);
    }

    public Shape addShapeElementToSlide(
            Long userId,
            Long slideId,
            BorderRef borderRef,
            Long x, Long y, Long z,
            Double angle,
            Long width, Long height,
            String path,
            String color
    ) {
        User user = userService.getUser(userId);
        Slide slide = getSlide(slideId, false);
        Design design = slide.getDesign();
        Workspace workspace = design.getWorkspace();
        // 해당 유저가 해당 워크스페이스, 디자인의 소유자인지 확인
        checkUWDS(user, workspace, design, slide);

        Shape shape = Shape.builder()
                .slide(slide)
                .borderRef(borderRef)
                .x(x)
                .y(y)
                .z(z)
                .angle(angle)
                .width(width)
                .height(height)
                .path(path)
                .color(color)
                .build();

        return elementRepository.save(shape);
    }

    public Image addImageElementToSlide(
            Long userId,
            Long slideId,
            BorderRef borderRef,
            Long x, Long y, Long z,
            Double angle,
            Long width, Long height,
            String s3Url
    ) {
        User user = userService.getUser(userId);
        Slide slide = getSlide(slideId, false);
        Design design = slide.getDesign();
        Workspace workspace = design.getWorkspace();
        // 해당 유저가 해당 워크스페이스, 디자인의 소유자인지 확인
        checkUWDS(user, workspace, design, slide);

        Image image = Image.builder()
                .slide(slide)
                .borderRef(borderRef)
                .x(x)
                .y(y)
                .z(z)
                .angle(angle)
                .width(width)
                .height(height)
                .url(s3Url)
                .build();

        return elementRepository.save(image);
    }

    public Spatial addSpatialElementToSlide(
            Long userId,
            Long slideId,
            BorderRef borderRef,
            Long x, Long y, Long z,
            Double angle,
            Long width, Long height,
            CameraMode cameraMode,
            CameraTransform cameraTransform,
            String content,
            String backgroundColor
    ) {
        User user = userService.getUser(userId);
        Slide slide = getSlide(slideId, false);
        Design design = slide.getDesign();
        Workspace workspace = design.getWorkspace();
        // 해당 유저가 해당 워크스페이스, 디자인의 소유자인지 확인
        checkUWDS(user, workspace, design, slide);

        Spatial spatial = Spatial.builder()
                .slide(slide)
                .borderRef(borderRef)
                .x(x)
                .y(y)
                .z(z)
                .angle(angle)
                .width(width)
                .height(height)
                .cameraMode(cameraMode)
                .cameraTransform(cameraTransform)
                .backgroundColor(backgroundColor)
                .build();

        if (content != null) {
            Model model = modelService.createNewModel(content);
            spatial.addModel(model);
        }

        return elementRepository.save(spatial);
    }

    private Slide getSlide(Long slideId, boolean withElementsFlag) {
        Optional<Slide> slideOptional;
        if (withElementsFlag) {
            slideOptional = slideRepository.findWithSlideElementListById(slideId);
        } else {
             slideOptional = slideRepository.findById(slideId);
        }
        if (slideOptional.isEmpty()) {
            throw new SlideNotFound();
        }
        return slideOptional.get();
    }

    public TextBox updateTextBox(
            Long userId,
            Long elementId,
            String text,
            long size,
            long weight,
            String fontFamily,
            TextAlign textAlign
    ) {
        Optional<TextBox> optionalElement = elementRepository.findTextBoxByIdAndUserId(elementId, userId);
        if (optionalElement.isEmpty()) {
            throw new ElementNotFound();
        }
        TextBox textBox = optionalElement.get();

        textBox.update(text, size, weight, fontFamily, textAlign);
        return textBox;
    }

    public Shape updateShape(
            Long userId,
            Long elementId,
            String path,
            String color
    ) {
        Optional<Shape> optionalShape = elementRepository.findShapeByIdAndUserId(elementId, userId);
        if (optionalShape.isEmpty()) {
            throw new ElementNotFound();
        }

        Shape shape = optionalShape.get();
        shape.update(path, color);
        return shape;
    }

    public Spatial updateSpatial(
            Long userId,
            Long elementId,
            CameraMode cameraMode,
            CameraTransform cameraTransform,
            String backgroundColor
    ){
        Optional<Spatial> optionalSpatial = elementRepository.findSpatialById(elementId, userId);
        if (optionalSpatial.isEmpty()) {
            throw new ElementNotFound();
        }

        Spatial spatial = optionalSpatial.get();
        spatial.update(cameraMode, cameraTransform, backgroundColor);
        return spatial;
    }

    public Element updateCommonFields(
            Long userId,
            Long elementId,
            BorderRef borderRef,
            long x, long y, long z,
            long width, long height,
            double angle
    ) {
        Optional<Element> optionalElement =  elementRepository.findElementById(elementId, userId);
        if (optionalElement.isEmpty()) {
            throw new ElementNotFound();
        }

        Element element = optionalElement.get();
        element.update(borderRef, x, y, z, width, height, angle);
        return element;
    }

    @Transactional(readOnly = true)
    public List<Element> findAllElementsInSlide(Long userId, Long slideId) {
        Slide slide = getSlide(slideId, true);
        User user = userService.getUser(userId);
        Design design = slide.getDesign();
        checkUWDS(user, null, design, slide);

        return slide.getSlideElementList();
    }

    public void delete(Long userId, Long elementId) {
        Optional<Element> optionalElement = elementRepository.findElementById(elementId, userId);
        if (optionalElement.isEmpty()) {
            throw new ElementNotFound();
        }
        Element element = optionalElement.get();
        deleteS3(element);
        elementRepository.delete(element);
    }

    protected void deleteS3(Element element) {
        if (element instanceof Spatial spatial) {
            for (Model model : spatial.getModels()) {
                String url = model.getUrl();
                if (url != null) {
                    s3Service.delete(url);
                }
            }
        } else if (element instanceof Image image) {
            String content = image.getUrl();
            s3Service.delete(content);
        }
    }
}
