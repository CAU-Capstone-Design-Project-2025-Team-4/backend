package com.capstone2025.team4.backend.exception.user;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class PasswordDoesntMatchException extends BusinessException {
    public PasswordDoesntMatchException() {
        super(ExceptionCode.USER_PASSWORD_INCONSISTENT);
    }
}