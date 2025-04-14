package com.capstone2025.team4.backend.controller.api;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ApiResponse<T> {
    public static final String OK_STATUS = "OK"; // 정상
    public static final String VALIDATION_ERROR_STATUS = "VALIDATION_ERROR"; // 검증 오류
    public static final String BUSINESS_EXCEPTION_STATUS = "BUSINESS_EXCEPTION"; // 예외 발생

    private final String status;
    private final T data;
    private final String message;

    private ApiResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(OK_STATUS, data, null);
    }

    public static ApiResponse<?> validationError(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        for (ObjectError error : bindingResult.getAllErrors()) {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), error.getDefaultMessage());
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        }

        return new ApiResponse<>(VALIDATION_ERROR_STATUS, errors, null);
    }

    public static ApiResponse<?> businessException(String message) {
        return new ApiResponse<>(BUSINESS_EXCEPTION_STATUS, null, message);
    }
}
