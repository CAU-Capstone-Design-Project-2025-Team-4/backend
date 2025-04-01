package com.capstone2025.team4.backend.controller.advice;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> argumentsNotValid(MethodArgumentNotValidException ex) {
        return ApiResponse.validationError(ex.getBindingResult());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> businessExceptions(BusinessException exception) {
        ExceptionCode exceptionCode = exception.getExceptionCode();
        HttpStatus httpStatus = exceptionCode.getHttpStatus();
        String message = exceptionCode.getMessage();

        return ResponseEntity.status(httpStatus).body(ApiResponse.businessException(message));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> badMethod(HttpRequestMethodNotSupportedException exception) {
        String method = exception.getMethod();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(method + " is Not Allowed");
    }
}
