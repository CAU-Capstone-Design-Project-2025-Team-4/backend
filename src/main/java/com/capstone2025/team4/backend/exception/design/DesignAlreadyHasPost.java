package com.capstone2025.team4.backend.exception.design;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class DesignAlreadyHasPost extends BusinessException {
    public DesignAlreadyHasPost() {
      super(ExceptionCode.DESIGN_ALREADY_HAS_POST);
    }
}
