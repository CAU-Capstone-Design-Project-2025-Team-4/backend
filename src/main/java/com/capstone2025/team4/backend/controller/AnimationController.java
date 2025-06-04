package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.api.animation.AddAnimationRequest;
import com.capstone2025.team4.backend.controller.api.animation.AnimationResponse;
import com.capstone2025.team4.backend.domain.animation.Animation;
import com.capstone2025.team4.backend.service.animation.AnimationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animation")
@RequiredArgsConstructor
public class AnimationController {

    private final AnimationService animationService;

    @PostMapping
    public ApiResponse<AnimationResponse> addAnimation(@RequestBody AddAnimationRequest request) {
        Animation animation = animationService.addAnimationToElement(request.getUserId(), request.getElementId(), request.getType(), request.getDuration(), request.getTiming());
        return ApiResponse.success(new AnimationResponse(animation));
    }

    @DeleteMapping
    public ApiResponse<String> deleteAnimation(@RequestParam Long elementId, @RequestParam Long animationId) {
        animationService.deleteAnimationFromElement(animationId, elementId);
        return ApiResponse.success("OK");
    }

    @GetMapping(params = "animationId")
    public ApiResponse<AnimationResponse> getAnimation(@RequestParam Long animationId) {
        Animation animation = animationService.getAnimation(animationId);
        return ApiResponse.success(new AnimationResponse(animation));
    }

    @GetMapping(params = "slideId")
    public ApiResponse<List<AnimationResponse>> getAnimationsInSlide(@RequestParam Long slideId) {
        List<Animation> animationsInSlide = animationService.getAnimationsInSlide(slideId);
        List<AnimationResponse> result = animationsInSlide.stream().map(AnimationResponse::new).toList();
        return ApiResponse.success(result);
    }
}
