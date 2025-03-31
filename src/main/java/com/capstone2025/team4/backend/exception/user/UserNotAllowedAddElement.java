package com.capstone2025.team4.backend.exception.user;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class UserNotAllowedAddElement extends BusinessException {

    public UserNotAllowedAddElement() {
        super(ExceptionCode.USER_NOT_ALLOWED);
    }
}
