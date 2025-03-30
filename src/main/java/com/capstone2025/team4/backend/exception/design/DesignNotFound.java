package com.capstone2025.team4.backend.exception.design;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class DesignNotFound extends BusinessException {

    public DesignNotFound() {
        super(ExceptionCode.DESIGN_NOT_FOUND);
    }
}
