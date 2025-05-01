package com.capstone2025.team4.backend.exception.user;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class UserNotAllowed extends BusinessException {

    public UserNotAllowed() {
        super(ExceptionCode.USER_NOT_ALLOWED);
    }
}
