package com.capstone2025.team4.backend.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private final ExceptionCode exceptionCode;
  public BusinessException(String message, ExceptionCode exceptionCode) {
    super(message);
    this.exceptionCode = exceptionCode;
  }

  public BusinessException(ExceptionCode exceptionCode) {
    super(exceptionCode.getMessage());
    this.exceptionCode = exceptionCode;
  }
}
