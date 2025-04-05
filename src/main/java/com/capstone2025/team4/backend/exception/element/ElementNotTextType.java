package com.capstone2025.team4.backend.exception.element;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class ElementNotTextType extends BusinessException {

    public ElementNotTextType() {
        super(ExceptionCode.ELEMENT_NOT_TEXT);
    }
}
