package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.design.*;
import com.capstone2025.team4.backend.domain.element.*;
import com.capstone2025.team4.backend.domain.element.border.BorderRef;
import com.capstone2025.team4.backend.domain.element.spatial.CameraMode;
import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import com.capstone2025.team4.backend.domain.element.model.Model;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.exception.element.ElementNotFound;
import com.capstone2025.team4.backend.infra.aws.S3Service;
import com.capstone2025.team4.backend.repository.element.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ElementService {

    private final ElementRepository elementRepository;
    private final SlideService slideService;
    private final ModelService modelService;
    private final S3Service s3Service;

    public TextBox addTextBoxElementToSlide(
            Long userId,
            Long slideId,
            BorderRef borderRef,
            Double x, Double y, Double z,
            Double angle,
            Double width, Double height,
            String text,
            Double size,
            Double weight,
            TextAlign align,
            String fontFamily
    ) {
        Slide slide = slideService.getSlide(userId, slideId);

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
            Double x, Double y, Double z,
            Double angle,
            Double width, Double height,
            String path,
            String color
    ) {
        Slide slide = slideService.getSlide(userId, slideId);

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
            Double x, Double y, Double z,
            Double angle,
            Double width, Double height,
            String s3Url
    ) {
        Slide slide = slideService.getSlide(userId, slideId);

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
            Double x, Double y, Double z,
            Double angle,
            Double width, Double height,
            CameraMode cameraMode,
            CameraTransform cameraTransform,
            String content,
            String backgroundColor
    ) {
        Slide slide = slideService.getSlide(userId, slideId);

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

    public TextBox updateTextBox(
            Long userId,
            Long elementId,
            String text,
            Double size,
            Double weight,
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
        Spatial spatial = getSpatial(userId, elementId);
        spatial.update(cameraMode, cameraTransform, backgroundColor);
        return spatial;
    }

    public Element updateCommonFields(
            Long userId,
            Long elementId,
            BorderRef borderRef,
//            long x, long y, long z,
//            long width, long height,
            double x, double y, double z,
            double width, double height,
            double angle
    ) {
        Element element = getElement(userId, elementId);
        element.update(borderRef, x, y, z, width, height, angle);
        return element;
    }

    public Element getElement(Long userId, Long elementId) {
        Optional<Element> optionalElement =  elementRepository.findElementById(elementId, userId);
        if (optionalElement.isEmpty()) {
            throw new ElementNotFound();
        }

        return optionalElement.get();
    }

    @Transactional(readOnly = true)
    public List<Element> findAllElementsInSlide(Long userId, Long slideId) {
        Slide slide = slideService.getSlideWithElements(userId, slideId);

        return slide.getSlideElementList();
    }

    public void delete(Long userId, Long elementId) {
        Element element = getElement(userId, elementId);
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

    public Element copyAndSaveElement(Element slideElement, Slide newSlide) {
        Element copy = slideElement.copy(newSlide);
        return elementRepository.save(copy);
    }

    public Spatial getSpatial(Long userId, Long elementId) {
        Optional<Spatial> optionalSpatial = elementRepository.findSpatialById(elementId, userId);
        if (optionalSpatial.isEmpty()) {
            throw new ElementNotFound();
        }

        return optionalSpatial.get();
    }
}
