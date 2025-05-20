package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.api.design.ChangeDesignNameRequest;
import com.capstone2025.team4.backend.controller.api.design.DesignLongResponse;
import com.capstone2025.team4.backend.controller.api.design.DesignShortResponse;
import com.capstone2025.team4.backend.controller.api.design.NewDesignRequest;
import com.capstone2025.team4.backend.domain.design.Design;
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

    @DeleteMapping
    public ApiResponse<String> delete(@RequestParam Long userId, @RequestParam Long designId) {
        designService.delete(userId, designId);
        return ApiResponse.success("OK");
    }

    @PatchMapping
    public ApiResponse<DesignShortResponse> changeName(@RequestBody ChangeDesignNameRequest request) {
        Design design = designService.changeName(request.getUserId(), request.getDesignId(), request.getName());
        return ApiResponse.success(new DesignShortResponse(design));
    }
}
