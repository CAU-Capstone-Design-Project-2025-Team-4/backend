package com.capstone2025.team4.backend.exception.element;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class ElementDefaultNotFound extends BusinessException {
    public ElementDefaultNotFound() {
        super(ExceptionCode.ELEMENT_DEFAULT_NOT_FOUND);
    }
}
