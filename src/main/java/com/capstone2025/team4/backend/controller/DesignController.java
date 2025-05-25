package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.api.design.*;
import com.capstone2025.team4.backend.controller.api.model.UpdateModelRequest;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.service.design.DesignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/design")
@RequiredArgsConstructor
public class DesignController {
    private final DesignService designService;

    @PostMapping
    public ApiResponse<DesignLongResponse> newDesign(@Valid @RequestBody NewDesignRequest request) {
        Design newDesign = designService.createNewDesign(request.getName(), request.getUserId(), request.getSourceId(), request.getIsShared());

        return ApiResponse.success(new DesignLongResponse(newDesign));
    }

    @GetMapping
    public ApiResponse<List<DesignShortResponse>> getAll(@RequestParam Long userId) {
        List<Design> all = designService.findAll(userId);
        List<DesignShortResponse> result = all.stream().map(DesignShortResponse::new).toList();
        return ApiResponse.success(result);
    }

    @GetMapping("/{designId}")
    public ApiResponse<DesignLongResponse> findDesign(@PathVariable Long designId) {
        Design design = designService.findDesign(designId, true);
        return ApiResponse.success(new DesignLongResponse(design));
    }

    @DeleteMapping
    public ApiResponse<String> delete(@RequestParam Long userId, @RequestParam Long designId) {
        designService.delete(userId, designId);
        return ApiResponse.success("OK");
    }

    @PatchMapping("/name")
    public ApiResponse<DesignShortResponse> changeName(@RequestBody ChangeDesignNameRequest request) {
        Design design = designService.changeName(request.getUserId(), request.getDesignId(), request.getName());
        return ApiResponse.success(new DesignShortResponse(design));
    }

    @PatchMapping("/thumbnail")
    public ApiResponse<DesignShortResponse> updateThumbnail(@Valid @ModelAttribute UpdateDesignThumbnailRequest request) {
        try {
            Design design = designService.changeThumbnail(request.getUserId(), request.getDesignId(), request.getImage().getBytes());
            return ApiResponse.success(new DesignShortResponse(design));
        } catch (IOException e) {
            log.error("이미지 바이트 배열을 처리하지 못함");
            throw new RuntimeException(e);
        }
    }
}
