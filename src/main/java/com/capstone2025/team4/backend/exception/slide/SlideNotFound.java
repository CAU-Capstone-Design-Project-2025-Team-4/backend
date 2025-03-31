package com.capstone2025.team4.backend.exception.slide;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class SlideNotFound extends BusinessException {

    public SlideNotFound() {
        super(ExceptionCode.SLIDE_NOT_FOUND);
    }
}
