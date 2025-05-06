package com.capstone2025.team4.backend.exception.slide;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class SlideOrderDuplicate extends BusinessException {
    public SlideOrderDuplicate() {
        super(ExceptionCode.SLIDE_ORDER_DUPLICATE);
    }
}
