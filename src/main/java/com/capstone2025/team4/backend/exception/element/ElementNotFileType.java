package com.capstone2025.team4.backend.exception.element;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class ElementNotFileType extends BusinessException {

    public ElementNotFileType() {
        super(ExceptionCode.ELEMENT_NOT_FILE);
    }
}
