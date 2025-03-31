package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.dto.design.ElementResponse;
import com.capstone2025.team4.backend.controller.dto.element.AddNewElementRequest;
import com.capstone2025.team4.backend.domain.design.Element;
import com.capstone2025.team4.backend.domain.design.SlideElement;
import com.capstone2025.team4.backend.exception.element.ElementFileNotFound;
import com.capstone2025.team4.backend.exception.element.ElementNotFound;
import com.capstone2025.team4.backend.infra.aws.S3Entity;
import com.capstone2025.team4.backend.infra.aws.S3Service;
import com.capstone2025.team4.backend.repository.ElementRepository;
import com.capstone2025.team4.backend.service.design.ElementService;
import com.capstone2025.team4.backend.utils.StringChecker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RestController
@RequestMapping("/element")
@RequiredArgsConstructor
@Slf4j
public class ElementController {
    private final ElementService elementService;
    private final S3Service s3Service;
    private final ElementRepository elementRepository;

    // 유저가 새로운 파일 형태의 요소 추가 : ex) 사진, 모델 등
    @PostMapping("/add")
    public ApiResponse<ElementResponse> addNewElement(@Valid @ModelAttribute AddNewElementRequest request) {
        String url = s3Service.upload(request.getFile());
        SlideElement slideElement = elementService.addUserElementToSlide(request.getUserId(), request.getSlideId(), url, request.getType(), request.getX(), request.getY(), request.getAngle(), request.getWidth(), request.getHeight());

        return ApiResponse.success(new ElementResponse(slideElement));
    }

    @GetMapping("/{elementId}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long elementId) {
        Optional<Element> optionalElement = elementRepository.findById(elementId);
        if (optionalElement.isEmpty()) {
            throw new ElementNotFound();
        }

        Element element = optionalElement.get();
        if (StringChecker.stringsAreEmpty(element.getUrl())) {
            throw new ElementFileNotFound();
        }

        S3Entity s3Entity = s3Service.findByUrl(element.getUrl());

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
}
