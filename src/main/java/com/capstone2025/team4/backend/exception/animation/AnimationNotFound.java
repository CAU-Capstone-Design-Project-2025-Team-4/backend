package com.capstone2025.team4.backend.exception.animation;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class AnimationNotFound extends BusinessException {
    public AnimationNotFound() {
        super(ExceptionCode.ANIMATION_NOT_FOUND);
    }
}
