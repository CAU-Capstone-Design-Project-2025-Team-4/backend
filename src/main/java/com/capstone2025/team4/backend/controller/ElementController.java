package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.dto.element.AddNewFileElementRequest;
import com.capstone2025.team4.backend.controller.dto.element.AddNewTextElementRequest;
import com.capstone2025.team4.backend.controller.dto.element.ElementResponse;
import com.capstone2025.team4.backend.controller.dto.element.UpdateElementRequest;
import com.capstone2025.team4.backend.domain.design.SlideElement;
import com.capstone2025.team4.backend.domain.design.Type;
import com.capstone2025.team4.backend.domain.design.element.FileElement;
import com.capstone2025.team4.backend.exception.element.ElementFileNotFound;
import com.capstone2025.team4.backend.exception.element.ElementNotFileType;
import com.capstone2025.team4.backend.exception.element.ElementNotFound;
import com.capstone2025.team4.backend.exception.element.ElementNotTextType;
import com.capstone2025.team4.backend.exception.file.FileIsEmpty;
import com.capstone2025.team4.backend.infra.aws.S3Entity;
import com.capstone2025.team4.backend.infra.aws.S3Service;
import com.capstone2025.team4.backend.repository.ElementRepository;
import com.capstone2025.team4.backend.repository.FileElementRepository;
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
    private final FileElementRepository fileElementRepository;

    // 유저가 새로운 파일 형태의 요소 추가 : ex) 사진, 모델 등
    @PostMapping("/file")
    public ApiResponse<ElementResponse> addNewFileElement(@Valid @ModelAttribute AddNewFileElementRequest request) {
        String url = s3Service.upload(request.getFile());
        Type type = request.getType();
        if (!type.isFileType()) {
            throw new ElementNotFileType();
        }
        if (request.getFile().isEmpty()) {
            throw new FileIsEmpty();
        }
        SlideElement slideElement = elementService.addUserElementToSlide(request.getUserId(), request.getSlideId(), url, request.getType(), request.getX(), request.getY(), request.getAngle(), request.getWidth(), request.getHeight());

        return ApiResponse.success(new ElementResponse(slideElement));
    }

    @PostMapping("/text")
    public ApiResponse<ElementResponse> addNewTextElement(@Valid @RequestBody AddNewTextElementRequest request) {
        Type type = request.getType();
        if (!type.isTextType()) {
            throw new ElementNotTextType();
        }

        SlideElement slideElement = elementService.addUserElementToSlide(request.getUserId(), request.getSlideId(), request.getContent(), request.getType(), request.getX(), request.getY(), request.getAngle(), request.getWidth(), request.getHeight());

        return ApiResponse.success(new ElementResponse(slideElement));
    }

    @PatchMapping
    public ApiResponse<ElementResponse> updateElement(@Valid @RequestBody UpdateElementRequest request) {
        SlideElement slideElement = elementService.updateSlideElement(request.getUserId(), request.getElementId(), request.getX(), request.getY(), request.getAngle(), request.getWidth(), request.getHeight());

        return ApiResponse.success(new ElementResponse(slideElement));
    }

    @GetMapping("/file/{elementId}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long elementId) {
        Optional<FileElement> optionalElement = fileElementRepository.findById(elementId);
        if (optionalElement.isEmpty()) {
            throw new ElementNotFound();
        }

        FileElement element = optionalElement.get();

        if (StringChecker.stringsAreEmpty(element.getS3Url())) {
            throw new ElementFileNotFound();
        }

        S3Entity s3Entity = s3Service.findByUrl(element.getS3Url());

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
        List<SlideElement> allElementsInSlide = elementService.getAllElementsInSlide(userId, slideId);

        List<ElementResponse> result = allElementsInSlide.stream()
                .map(ElementResponse::new)
                .collect(Collectors.toList());

        return ApiResponse.success(result);
    }
}
