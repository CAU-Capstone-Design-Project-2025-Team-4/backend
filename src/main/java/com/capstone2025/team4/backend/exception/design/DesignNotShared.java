package com.capstone2025.team4.backend.exception.design;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class DesignNotShared extends BusinessException {
    public DesignNotShared() {
        super(ExceptionCode.DESIGN_NOT_SHARED);
    }
}
