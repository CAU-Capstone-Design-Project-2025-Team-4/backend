package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.api.design.*;
import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.service.UserService;
import com.capstone2025.team4.backend.service.WorkspaceService;
import com.capstone2025.team4.backend.service.design.DesignService;
import com.capstone2025.team4.backend.service.design.SlideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/slide")
@RequiredArgsConstructor
@Slf4j
public class SlideController {

    private final SlideService slideService;
    private final UserService userService;
    private final DesignService designService;
    private final WorkspaceService workspaceService;

    @PostMapping
    public ApiResponse<SlideWithElementResponse> newSlide(@Valid @RequestBody NewSlideRequest request) {
        User user = userService.getUser(request.getUserId());

        Workspace workspace = workspaceService.getWorkspace(user);

        Design design = designService.getDesign(request.getDesignId());

        Slide newSlide = slideService.newSlide(user, design, workspace, request.getOrder());
        return ApiResponse.success(new SlideWithElementResponse(newSlide));
    }

    @GetMapping
    public ApiResponse<AllSlidesInDesignResponse> allInDesign(@RequestParam Long designId, @RequestParam Long userId) {
        List<Slide> slides = slideService.findAllInDesign(userId, designId);

        AllSlidesInDesignResponse result = new AllSlidesInDesignResponse(slides);
        return ApiResponse.success(result);
    }

    @DeleteMapping
    public ApiResponse<String> delete(@RequestParam Long slideId, @RequestParam Long userId) {
        slideService.delete(userId, slideId);
        return ApiResponse.success("OK");
    }

    @PatchMapping("/thumbnail")
    public ApiResponse<ShortSlideResponse> changeThumbnail(UpdateSlideThumbnailRequest request) {
        try {
            Slide slide = slideService.changeThumbnail(request.getUserId(), request.getDesignId(), request.getSlideId(), request.getImage().getBytes());
            return ApiResponse.success(new ShortSlideResponse(slide));
        } catch (IOException e) {
            log.error("이미지 바이트 배열을 처리하지 못함");
            throw new RuntimeException(e);
        }
    }
}
