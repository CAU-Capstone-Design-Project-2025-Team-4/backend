package com.capstone2025.team4.backend.exception.model;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class ModelNotFound extends BusinessException {
    public ModelNotFound() {
        super(ExceptionCode.MODEL_NOT_FOUNT);
    }
}
