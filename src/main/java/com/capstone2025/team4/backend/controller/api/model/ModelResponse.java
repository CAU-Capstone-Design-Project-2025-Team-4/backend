package com.capstone2025.team4.backend.controller.api.model;

import com.capstone2025.team4.backend.domain.element.model.Model;
import com.capstone2025.team4.backend.domain.element.model.ModelShader;
import com.capstone2025.team4.backend.domain.element.model.ModelTransform;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ModelResponse {
    private Long id;
    private String url;
    private String name;
    private ModelShader shader;
    private ModelTransform modelTransform;


   public static ModelResponse createForm(Model model) {
       ModelResponse modelResponse = new ModelResponse();
       modelResponse.id = model.getId();
       modelResponse.name = model.getName();
       modelResponse.url = model.getUrl();
       modelResponse.shader = model.getShader();
       modelResponse.modelTransform = model.getModelTransform();
       return modelResponse;
   }
}
