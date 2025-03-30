package com.capstone2025.team4.backend.exception.user;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class UserNotAllowedDesign extends BusinessException {

    public UserNotAllowedDesign() {
        super(ExceptionCode.USER_NOT_ALLOWED_DESIGN);
    }
}
