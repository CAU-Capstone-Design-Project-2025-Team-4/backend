package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.api.model.AddModelRequest;
import com.capstone2025.team4.backend.controller.api.model.ModelResponse;
import com.capstone2025.team4.backend.domain.element.spatial.Model;
import com.capstone2025.team4.backend.service.design.ModelService;
import com.capstone2025.team4.backend.service.dto.FileDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/model")
public class ModelController {

    private final ModelService modelService;

    @GetMapping("/{modelId}")
    public ResponseEntity<byte[]> getModel(@PathVariable Long modelId) {
        FileDTO fileDTO = modelService.getFile(modelId);
        String fileName = fileDTO.getFileName();
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

        byte[] fileBytes = fileDTO.getFileBytes();
        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    @PostMapping
    public ApiResponse<ModelResponse> addModel(@Valid @ModelAttribute AddModelRequest request) {
        Model model = modelService.addModel(request.getSpatialId(), request.getUserId(), request.getFile());
        return ApiResponse.success(new ModelResponse(model.getId(), model.getUrl()));
    }

    @DeleteMapping("/{modelId}")
    public ApiResponse<String> delete(@PathVariable Long modelId) {
        modelService.delete(modelId);
        return ApiResponse.success("OK");
    }
}
