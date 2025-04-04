package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.dto.design.NewSlideRequest;
import com.capstone2025.team4.backend.controller.dto.design.SlideWithElementResponse;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.service.design.DesignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slide")
@RequiredArgsConstructor
public class SlideController {

    private final DesignService designService;

    @PostMapping("/new")
    public ApiResponse<SlideWithElementResponse> newSlide(@Valid @RequestBody NewSlideRequest request) {
        Slide newSlide = designService.newSlide(request.getUserId(), request.getDesignId(), request.getOrder());
        return ApiResponse.success(new SlideWithElementResponse(newSlide));
    }
}
