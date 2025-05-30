package com.capstone2025.team4.backend.exception.template;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class TemplateNotFound extends BusinessException {
    public TemplateNotFound() {
        super(ExceptionCode.TEMPLATE_NOT_FOUND);
    }
}
