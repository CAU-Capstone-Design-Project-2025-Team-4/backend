package com.capstone2025.team4.backend.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ApiResponse {

    public static <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
