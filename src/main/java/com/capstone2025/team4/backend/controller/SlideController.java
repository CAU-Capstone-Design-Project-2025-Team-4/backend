package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.dto.design.AllSlidesInDesignResponse;
import com.capstone2025.team4.backend.controller.dto.design.NewSlideRequest;
import com.capstone2025.team4.backend.controller.dto.design.SlideWithElementResponse;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.service.design.SlideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/slide")
@RequiredArgsConstructor
public class SlideController {

    private final SlideService slideService;

    @PostMapping
    public ApiResponse<SlideWithElementResponse> newSlide(@Valid @RequestBody NewSlideRequest request) {
        Slide newSlide = slideService.newSlide(request.getUserId(), request.getDesignId(), request.getOrder());
        return ApiResponse.success(new SlideWithElementResponse(newSlide));
    }

    @GetMapping
    public ApiResponse<AllSlidesInDesignResponse> allInDesign(@RequestParam Long designId, Long userId) {
        List<Slide> slides = slideService.findAllInDesign(userId, designId);

        AllSlidesInDesignResponse result = new AllSlidesInDesignResponse(slides);
        return ApiResponse.success(result);
    }
}
