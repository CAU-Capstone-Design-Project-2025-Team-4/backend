package com.capstone2025.team4.backend.exception.frame;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class FrameNotFound extends BusinessException {
    public FrameNotFound() {
        super(ExceptionCode.FRAME_NOT_FOUND);
    }
}
