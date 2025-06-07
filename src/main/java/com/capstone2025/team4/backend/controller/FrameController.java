package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.api.frame.AddFrameRequest;
import com.capstone2025.team4.backend.controller.api.frame.FrameResponse;
import com.capstone2025.team4.backend.controller.api.frame.UpdateFrameRequest;
import com.capstone2025.team4.backend.domain.element.spatial.Frame;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.service.design.ElementService;
import com.capstone2025.team4.backend.service.design.FrameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/frame")
@RequiredArgsConstructor
public class FrameController {

    private final ElementService elementService;
    private final FrameService frameService;

    @PostMapping
    public ApiResponse<FrameResponse> addFrame(@RequestBody AddFrameRequest request) {
        Spatial spatial = elementService.getSpatial(request.getUserId(), request.getSpatialId());
        Frame frame = frameService.addFrame(spatial, request.getName(), request.getCameraTransform());
        return ApiResponse.success(new FrameResponse(frame));
    }

    @DeleteMapping
    public ApiResponse<String> deleteFrame(@RequestParam Long userId,
                                           @RequestParam Long spatialId,
                                           @RequestParam Long frameId) {
        Spatial spatial = elementService.getSpatial(userId, spatialId);
        frameService.deleteFrame(spatial, frameId);
        return ApiResponse.success("OK");
    }

    @PatchMapping
    public ApiResponse<FrameResponse> updateFrame(@RequestBody UpdateFrameRequest request) {
        Spatial spatial = elementService.getSpatial(request.getUserId(), request.getSpatialId());
        Frame frame = frameService.updateFrame(spatial,
                request.getFrameId(),
                request.getName(),
                request.getCameraTransform());

        return ApiResponse.success(new FrameResponse(frame));
    }

    @GetMapping
    public ApiResponse<FrameResponse> getFrame(@RequestParam Long userId,
                                               @RequestParam Long spatialId,
                                               @RequestParam Long frameId) {
        Spatial spatial = elementService.getSpatial(userId, spatialId);
        Frame frame = frameService.getFrame(spatial, frameId);
        return ApiResponse.success(new FrameResponse(frame));
    }

    @GetMapping("/all")
    public ApiResponse<List<FrameResponse>> findAllInSpatial(@RequestParam Long userId, @RequestParam Long spatialId) {
        Spatial spatial = elementService.getSpatial(userId, spatialId);
        List<FrameResponse> result = frameService.findAllInSpatial(spatial).stream()
                .map(FrameResponse::new).toList();
        return ApiResponse.success(result);
    }
}