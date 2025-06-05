package com.capstone2025.team4.backend.exception.element;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class ElementNotSpatial extends BusinessException {
    public ElementNotSpatial() {
      super(ExceptionCode.ELEMENT_NOT_SPATIAL);
    }
}
