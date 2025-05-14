package com.capstone2025.team4.backend.controller.api.model;

import com.capstone2025.team4.backend.domain.element.spatial.Model;
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


   public static ModelResponse createForm(Model model) {
       ModelResponse modelResponse = new ModelResponse();
       modelResponse.id = model.getId();
       modelResponse.url = model.getUrl();
       return modelResponse;
   }
}
