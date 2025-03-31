package com.capstone2025.team4.backend.infra.aws;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(name = "S3ENTITY_URL_UNIQUE", columnNames = {"url"}), @UniqueConstraint(name = "S3ENTITY_KEY_UNIQUE", columnNames = {"key"})})
@Getter
public class S3Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String s3Key;

    private String originalFileName;

    @Builder
    private S3Entity(String url, String s3Key, String originalFileName) {
        this.url = url;
        this.s3Key = s3Key;
        this.originalFileName = originalFileName;
    }
}

