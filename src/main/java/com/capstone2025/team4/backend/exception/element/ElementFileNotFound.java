package com.capstone2025.team4.backend.exception.element;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class ElementFileNotFound extends BusinessException {

    public ElementFileNotFound() {
        super(ExceptionCode.ELEMENT_FILE_NOT_FOUND);
    }
}
