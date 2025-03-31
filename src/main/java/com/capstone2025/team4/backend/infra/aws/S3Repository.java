package com.capstone2025.team4.backend.infra.aws;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface S3Repository extends JpaRepository<S3Entity, Long> {
    Optional<S3Entity> findByUrl(String url);
}
