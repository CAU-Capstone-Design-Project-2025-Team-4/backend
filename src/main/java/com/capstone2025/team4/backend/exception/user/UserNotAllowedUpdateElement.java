package com.capstone2025.team4.backend.exception.user;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class UserNotAllowedUpdateElement extends BusinessException {

    public UserNotAllowedUpdateElement() {
        super("해당 사용자는 요소를 업데이트 할 권한이 없습니다!",ExceptionCode.USER_NOT_ALLOWED);
    }
}
