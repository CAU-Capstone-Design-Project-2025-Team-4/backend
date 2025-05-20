package com.capstone2025.team4.backend.exception.post;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class EmptyPostTitle extends BusinessException {
  public EmptyPostTitle() {
    super(ExceptionCode.POST_TITLE_EMPTY);
  }
}
