package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.dto.design.NewDesignRequest;
import com.capstone2025.team4.backend.controller.dto.design.DesignWithSlidesResponse;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.service.DesignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
