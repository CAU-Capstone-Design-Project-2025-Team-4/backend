package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.element.model.Model;
import com.capstone2025.team4.backend.domain.element.model.ModelShader;
import com.capstone2025.team4.backend.domain.element.model.ModelTransform;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.exception.element.ElementFileNotFound;
import com.capstone2025.team4.backend.exception.element.ElementNotFound;
import com.capstone2025.team4.backend.infra.aws.S3Service;
import com.capstone2025.team4.backend.repository.model.ModelRepository;
import com.capstone2025.team4.backend.repository.element.ElementRepository;
import com.capstone2025.team4.backend.service.dto.FileDTO;
import com.capstone2025.team4.backend.utils.StringChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ModelService {
    private final ModelRepository modelRepository;
    private final S3Service s3Service;
    private final ElementRepository elementRepository;

    public Model createNewModel(String url) {
        Model model = Model.builder()
                .url(url)
                .build();
        modelRepository.save(model);
        return model;
    }

    @Transactional(readOnly = true)
    public FileDTO getFile(Long modelId) {
        Optional<Model> optionalModel = modelRepository.findById(modelId);
        if (optionalModel.isEmpty()) {
            throw new ElementFileNotFound();
        }
        Model model = optionalModel.get();
        String s3Url = model.getUrl();

        if (StringChecker.stringsAreEmpty(s3Url)) {
            throw new ElementFileNotFound();
        }

        return s3Service.findByUrl(s3Url);
    }

    public Model addModel(Long spatialId, Long userId, MultipartFile file, String name, ModelShader shader, ModelTransform modelTransform) {
        Spatial spatialElement = elementRepository.findSpatialById(spatialId, userId)
                .orElseThrow(ElementNotFound::new);
        String s3Url = s3Service.upload(file);
        Model model = Model.builder()
                .url(s3Url)
                .name(name)
                .modelTransform(modelTransform)
                .shader(shader)
                .build();
        spatialElement.addModel(model);

        return modelRepository.save(model);
    }

    public void delete(Long modelId) {
        Model model = modelRepository.findById(modelId)
                .orElseThrow(ElementFileNotFound::new);
        String url = model.getUrl();
        s3Service.delete(url);
        modelRepository.delete(model);
    }
}
