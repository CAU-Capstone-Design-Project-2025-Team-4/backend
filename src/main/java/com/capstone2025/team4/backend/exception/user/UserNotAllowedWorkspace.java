package com.capstone2025.team4.backend.exception.user;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class UserNotAllowedWorkspace extends BusinessException {

    public UserNotAllowedWorkspace() {
        super(ExceptionCode.USER_NOT_ALLOWED_WORKSPACE);
    }
}
