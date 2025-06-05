package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.api.animation.AddAnimation3dRequest;
import com.capstone2025.team4.backend.controller.api.animation.AddAnimationRequest;
import com.capstone2025.team4.backend.controller.api.animation.Animation3dResponse;
import com.capstone2025.team4.backend.controller.api.animation.AnimationResponse;
import com.capstone2025.team4.backend.domain.animation.Animation;
import com.capstone2025.team4.backend.domain.animation3d.Animation3D;
import com.capstone2025.team4.backend.service.animation.Animation3dService;
import com.capstone2025.team4.backend.service.animation.AnimationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animation")
@RequiredArgsConstructor
public class AnimationController {

    private final AnimationService animationService;
    private final Animation3dService animation3dService;

    @PostMapping
    public ApiResponse<AnimationResponse> addAnimation(@RequestBody AddAnimationRequest request) {
        Animation animation = animationService.addAnimationToElement(request.getUserId(), request.getElementId(), request.getType(), request.getDuration(), request.getTiming());
        return ApiResponse.success(new AnimationResponse(animation));
    }

    @PostMapping("/3d")
    public ApiResponse<Animation3dResponse> addAnimation3d(@RequestBody AddAnimation3dRequest request) {
        Animation3D animation = animation3dService.addAnimationToElement(request.getUserId(), request.getElementId(), request.getType());
        return ApiResponse.success(new Animation3dResponse(animation));
    }

    @DeleteMapping
    public ApiResponse<String> deleteAnimation(@RequestParam Long elementId, @RequestParam Long animationId) {
        animationService.deleteAnimationFromElement(animationId, elementId);
        return ApiResponse.success("OK");
    }

    @DeleteMapping("/3d")
    public ApiResponse<String> deleteAnimation3d(@RequestParam Long elementId, @RequestParam Long animationId) {
        animation3dService.deleteAnimationFromElement(animationId, elementId);
        return ApiResponse.success("OK");
    }

    @GetMapping(params = "animationId")
    public ApiResponse<AnimationResponse> getAnimation(@RequestParam Long animationId) {
        Animation animation = animationService.getAnimation(animationId);
        return ApiResponse.success(new AnimationResponse(animation));
    }

    @GetMapping(value = "/3d",params = "animationId")
    public ApiResponse<Animation3dResponse> getAnimation3d(@RequestParam Long animationId) {
        Animation3D animation = animation3dService.getAnimation(animationId);
        return ApiResponse.success(new Animation3dResponse(animation));
    }

    @GetMapping(params = "slideId")
    public ApiResponse<List<AnimationResponse>> getAnimationsInSlide(@RequestParam Long slideId) {
        List<Animation> animationsInSlide = animationService.getAnimationsInSlide(slideId);
        List<AnimationResponse> result = animationsInSlide.stream().map(AnimationResponse::new).toList();
        return ApiResponse.success(result);
    }

    @GetMapping(value = "/3d", params = "slideId")
    public ApiResponse<List<Animation3dResponse>> getAnimations3dInSlide(@RequestParam Long slideId) {
        List<Animation3D> animationsInSlide = animation3dService.getAnimationsInSlide(slideId);
        List<Animation3dResponse> result = animationsInSlide.stream().map(Animation3dResponse::new).toList();
        return ApiResponse.success(result);
    }
}
