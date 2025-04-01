package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.dto.design.*;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.service.design.DesignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/design")
@RequiredArgsConstructor
public class DesignController {
    private final DesignService designService;

    @PostMapping("/new")
    public ApiResponse<DesignWithSlidesResponse> newDesign(@Valid @RequestBody NewDesignRequest request) {
        Design newDesign = designService.createNewDesign(request.getUserId(), request.getSourceId(), request.getIsShared());

        return ApiResponse.success(new DesignWithSlidesResponse(newDesign));
    }

    @PostMapping("/slide/new")
    public ApiResponse<SlideResponse> newSlide(@Valid @RequestBody NewSlideRequest request) {
        Slide newSlide = designService.newSlide(request.getUserId(), request.getDesignId(), request.getOrder());
        return ApiResponse.success(new SlideResponse(newSlide));
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<DesignWithoutSlidesResponse>> getAll(@PathVariable Long userId) {
        List<Design> all = designService.findAll(userId);
        List<DesignWithoutSlidesResponse> result = all.stream().map(DesignWithoutSlidesResponse::new).toList();
        return ApiResponse.success(result);
    }
}
