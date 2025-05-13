package com.capstone2025.team4.backend.controller.api.element.response;

import com.capstone2025.team4.backend.controller.api.model.ModelResponse;
import com.capstone2025.team4.backend.controller.api.element.ElementType;
import com.capstone2025.team4.backend.domain.element.spatial.CameraMode;
import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import com.capstone2025.team4.backend.domain.element.spatial.Model;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SpatialResponse extends ElementResponse {
    private CameraMode cameraMode;
    private CameraTransform cameraTransform;
    private List<ModelResponse> models = new ArrayList<>();
    private String backgroundColor;
    private final ElementType type = ElementType.SPATIAL;

    public static SpatialResponse createFrom(Spatial spatial) {
        SpatialResponse spatialDTO = new SpatialResponse();
        spatialDTO.cameraMode = spatial.getCameraMode();
        spatialDTO.cameraTransform = spatial.getCameraTransform();
        for (Model model : spatial.getModels()) {
            ModelResponse modelResponse = ModelResponse.createForm(model);
            spatialDTO.models.add(modelResponse);
        }
        spatialDTO.backgroundColor = spatial.getBackgroundColor();
        return spatialDTO;
    }
}
