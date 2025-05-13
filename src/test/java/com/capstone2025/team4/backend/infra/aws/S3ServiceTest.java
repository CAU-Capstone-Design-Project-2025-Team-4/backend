package com.capstone2025.team4.backend.infra.aws;

import com.capstone2025.team4.backend.exception.element.ElementFileNotFound;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class S3ServiceTest {
    @Autowired
    S3Service s3Service;

    @Autowired
    EntityManager em;

    @Test
    void delete() throws IOException {
        //given
        String testFile = "testFile";
        InputStream inputStream =
                new ByteArrayInputStream(testFile.getBytes(StandardCharsets.UTF_8));
        String tempKey = "tempKey";
        String testUrl = s3Service.storeFileInS3("text/plain", tempKey, inputStream);

        S3Entity s3Entity = S3Entity.builder()
                .url(testUrl)
                .s3Key(tempKey).build();

        em.persist(s3Entity);

        em.flush();
        em.clear();

        //when
        s3Service.delete(testUrl);

        //then
        assertThrows(ElementFileNotFound.class, () -> s3Service.findByUrl(testUrl));
    }
}