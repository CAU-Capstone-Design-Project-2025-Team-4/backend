package com.capstone2025.team4.backend.controller.advice;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;
import com.capstone2025.team4.backend.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse argumentsNotValid(MethodArgumentNotValidException ex) {
        List<String> messages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> messages.add(error.getDefaultMessage()));
        return new ExceptionResponse(messages);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> businessExceptions(BusinessException exception) {
        ExceptionCode exceptionCode = exception.getExceptionCode();
        HttpStatus httpStatus = exceptionCode.getHttpStatus();
        String message = exceptionCode.getMessage();

        return new ResponseEntity<>(new ExceptionResponse(List.of(message)), httpStatus);
    }
}
