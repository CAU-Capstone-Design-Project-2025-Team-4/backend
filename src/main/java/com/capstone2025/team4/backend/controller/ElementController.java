package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.dto.element.request.*;
import com.capstone2025.team4.backend.controller.dto.element.response.ElementResponse;
import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.domain.element.Image;
import com.capstone2025.team4.backend.domain.element.Shape;
import com.capstone2025.team4.backend.domain.element.TextBox;
import com.capstone2025.team4.backend.domain.element.border.BorderRef;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.exception.element.ElementFileNotFound;
import com.capstone2025.team4.backend.exception.element.ElementNotFileType;
import com.capstone2025.team4.backend.exception.element.ElementNotFound;
import com.capstone2025.team4.backend.exception.file.FileIsEmpty;
import com.capstone2025.team4.backend.infra.aws.S3Entity;
import com.capstone2025.team4.backend.infra.aws.S3Service;
import com.capstone2025.team4.backend.repository.element.ElementRepository;
import com.capstone2025.team4.backend.service.design.ElementService;
import com.capstone2025.team4.backend.utils.StringChecker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/element")
@RequiredArgsConstructor
@Slf4j
public class ElementController {
    private final ElementService elementService;
    private final S3Service s3Service;
    private final ElementRepository elementRepository;

    @PostMapping("/textbox")
    public ApiResponse<ElementResponse> addNewTextBoxElement(@Valid @ModelAttribute AddTextRequest request) {
        BorderRef borderRef = BorderRef.builder()
                .borderType(request.getBorderType())
                .color(request.getBorderColor())
                .thickness(request.getBorderThickness())
                .build();
        TextBox textBox = elementService.addTextBoxElementToSlide(request.getUserId(), request.getSlideId(), borderRef, request.getX(), request.getY(), request.getZ(), request.getAngle(), request.getWidth(), request.getHeight(), request.getText(), request.getSize(), request.getWeight());
        return ApiResponse.success(ElementResponse.create(textBox));
    }

    @PostMapping("/shape")
    public ApiResponse<ElementResponse> addNewShapeElement(@Valid @ModelAttribute AddShapeRequest request) {
        BorderRef borderRef = BorderRef.builder()
                .borderType(request.getBorderType())
                .color(request.getBorderColor())
                .thickness(request.getBorderThickness())
                .build();
        Shape shape = elementService.addShapeElementToSlide(request.getUserId(), request.getSlideId(), borderRef, request.getX(), request.getY(), request.getZ(), request.getAngle(), request.getWidth(), request.getHeight(), request.getPath(), request.getColor());

        return ApiResponse.success(ElementResponse.create(shape));
    }

    @PostMapping("/image")
    public ApiResponse<ElementResponse> addNewImageElement(@Valid @ModelAttribute AddImageRequest request) {
        if (request.getImage().isEmpty()) {
            throw new FileIsEmpty();
        }
        String url = s3Service.upload(request.getImage());
        BorderRef borderRef = BorderRef.builder()
                .borderType(request.getBorderType())
                .color(request.getBorderColor())
                .thickness(request.getBorderThickness())
                .build();
        Image image = elementService.addImageElementToSlide(request.getUserId(), request.getSlideId(), borderRef, request.getX(), request.getY(), request.getZ(), request.getAngle(), request.getWidth(), request.getHeight(), url);

        return ApiResponse.success(ElementResponse.create(image));
    }

    @PostMapping("/spatial")
    public ApiResponse<ElementResponse> addSpatialElement(@Valid @ModelAttribute AddSpatialRequest request) {
        if (request.getFile().isEmpty()) {
            throw new FileIsEmpty();
        }

        BorderRef borderRef = BorderRef.builder()
                .borderType(request.getBorderType())
                .color(request.getBorderColor())
                .thickness(request.getBorderThickness())
                .build();
        String url = s3Service.upload(request.getFile());
        Spatial spatial = elementService.addSpatialElementToSlide(request.getUserId(), request.getSlideId(), borderRef, request.getX(), request.getY(), request.getZ(), request.getAngle(), request.getWidth(), request.getHeight(), request.getCameraMode(), request.getCameraTransform(), url, request.getBackgroundColor());
        return ApiResponse.success(ElementResponse.create(spatial));
    }

    // 요소의 공통 속성 업데이트
    @PatchMapping
    public ApiResponse<ElementResponse> updateElement(@Valid @RequestBody UpdateElementRequest request) {
        Element element = elementService.updateCommonFields(request.getUserId(), request.getElementId(), request.getBorderRef(), request.getX(), request.getY(), request.getZ(), request.getWidth(), request.getHeight(), request.getAngle());

        return ApiResponse.success(ElementResponse.create(element));
    }

    //특정 요소의 속성 업데이트
    @PatchMapping("/textbox")
    public ApiResponse<ElementResponse> updateTextBox(@Valid @RequestBody UpdateTextBoxRequest request) {
        TextBox textBox = elementService.updateTextBox(request.getUserId(), request.getElementId(), request.getText(), request.getSize(), request.getWeight());
        return ApiResponse.success(ElementResponse.create(textBox));
    }

    @PatchMapping("/shape")
    public ApiResponse<ElementResponse> updateShape(@Valid @RequestBody UpdateShapeRequest request) {
        Shape shape = elementService.updateShape(request.getUserId(), request.getElementId(), request.getPath(), request.getColor());
        return ApiResponse.success(ElementResponse.create(shape));
    }

    @PatchMapping("/spatial")
    public ApiResponse<ElementResponse> updateSpatial(@Valid @RequestBody UpdateSpatialRequest request) {
        Spatial spatial = elementService.updateSpatial(request.getUserId(), request.getElementId(), request.getCameraMode(), request.getCameraTransform(), request.getContent(), request.getBackgroundColor());
        return ApiResponse.success(ElementResponse.create(spatial));
    }

    @GetMapping("/file/{elementId}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long elementId) {
        Optional<Element> optionalElement = elementRepository.findById(elementId);
        if (optionalElement.isEmpty()) {
            throw new ElementNotFound();
        }

        Element element = optionalElement.get();
        String s3Url;
        if (element instanceof Image image) {
            s3Url = image.getContent();
        } else if (element instanceof Spatial spatial) {
            s3Url = spatial.getContent();
        } else {
            throw new ElementNotFileType();
        }

        if (StringChecker.stringsAreEmpty(s3Url)) {
            throw new ElementFileNotFound();
        }

        S3Entity s3Entity = s3Service.findByUrl(s3Url);

        byte[] fileBytes = s3Service.download(s3Entity);
        String fileName = s3Entity.getOriginalFileName();
        String contentType = URLConnection.guessContentTypeFromName(fileName);
        if (contentType == null) {
            // GLB/GLTF 등 특수 타입 수동 지정
            if (fileName.endsWith(".glb")) {
                contentType = "model/gltf-binary";
            } else if (fileName.endsWith(".gltf")) {
                contentType = "model/gltf+json";
            } else if (fileName.endsWith(".wasm")) {
                contentType = "application/wasm";
//            }
//            else if (fileName.endsWith(".assetbundle")) {
//                contentType = "application/octet-stream"; // Unity 용도
            } else {
                contentType = "application/octet-stream"; // fallback
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDisposition(ContentDisposition.inline().filename(fileName, StandardCharsets.UTF_8).build());

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    @GetMapping
    public ApiResponse<List<ElementResponse>> getAllElementsInSlide(@RequestParam Long userId, Long slideId) {
        List<Element> allElementsInSlide = elementService.findAllElementsInSlide(userId, slideId);

        List<ElementResponse> result = allElementsInSlide.stream()
                .map(ElementResponse::create)
                .collect(Collectors.toList());

        return ApiResponse.success(result);
    }
}
