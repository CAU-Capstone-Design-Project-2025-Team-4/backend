package com.capstone2025.team4.backend.exception.design;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class DesignSourceNotFound extends BusinessException {

    public DesignSourceNotFound() {
        super(ExceptionCode.DESIGN_SOURCE_NOT_FOUND);
    }
}
