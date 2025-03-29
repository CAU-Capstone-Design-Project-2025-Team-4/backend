package com.capstone2025.team4.backend.exception.user;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class UserNotFoundException extends BusinessException {

  public UserNotFoundException() {
    super(ExceptionCode.USER_NOT_FOUND);
  }
}
