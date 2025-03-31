package com.capstone2025.team4.backend.exception.element;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class SlideElementNotFound extends BusinessException {

    public SlideElementNotFound() {
        super(ExceptionCode.SLIDE_ELEMENT_NOT_FOUND);
    }
}
