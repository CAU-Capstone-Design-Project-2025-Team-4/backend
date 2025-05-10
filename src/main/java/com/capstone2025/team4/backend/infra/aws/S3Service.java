package com.capstone2025.team4.backend.infra.aws;

import com.capstone2025.team4.backend.exception.element.ElementFileNotFound;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class S3Service {
    @Value("${spring.cloud.aws.s3.bucket}")
    private String BUCKET;
    private final S3Operations s3Operations;
    private final S3Repository s3Repository;

    public String upload(MultipartFile multipartFile){

        if (multipartFile.isEmpty()) {
            throw new RuntimeException("MultipartFile is Empty");
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String fileFormat = getFileFormat(originalFilename);
        String key = UUID.randomUUID().toString() + "." + fileFormat;

        try (InputStream is = multipartFile.getInputStream()) {
            ObjectMetadata objectMetadata = ObjectMetadata.builder().contentType(multipartFile.getContentType()).build();
            S3Resource s3Resource = s3Operations.upload(BUCKET, key, is,
                    objectMetadata);

            String url = s3Resource.getURL().toString();
            S3Entity s3Entity = S3Entity.builder()
                    .s3Key(key)
                    .url(url)
                    .originalFileName(originalFilename)
                    .build();
            s3Repository.save(s3Entity);

            return url;
        } catch (IOException e) {
            log.error("Upload to S3 failed", e);
            throw new RuntimeException(e);
        }
    }

    private String getFileFormat(String originalFilename) {
        assert originalFilename != null;
        int index = originalFilename.lastIndexOf(".");
        return originalFilename.substring(index+1);
    }

    @Transactional(readOnly = true)
    public S3Entity findByUrl(String url) {
        Optional<S3Entity> optionalS3Entity = s3Repository.findByUrl(url);
        if (optionalS3Entity.isEmpty()) {
            throw new ElementFileNotFound();
        }

        return optionalS3Entity.get();
    }

    public byte[] download(S3Entity s3Entity) {
        try {
            return s3Operations.download(BUCKET, s3Entity.getS3Key()).getContentAsByteArray();
        } catch (IOException e) {
            log.error("Cannot get file from S3Resource");
            throw new RuntimeException(e);
        }
    }

    public void delete(String url) {
        Optional<S3Entity> optionalS3Entity = s3Repository.findByUrl(url);
        if (optionalS3Entity.isEmpty()) {
            throw new ElementFileNotFound();
        }
        S3Entity s3Entity = optionalS3Entity.get();
        s3Operations.deleteObject(BUCKET, s3Entity.getS3Key());
    }
}
