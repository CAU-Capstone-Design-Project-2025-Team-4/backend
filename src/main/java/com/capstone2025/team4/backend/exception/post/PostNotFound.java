package com.capstone2025.team4.backend.exception.post;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class PostNotFound extends BusinessException {

    public PostNotFound() {
        super(ExceptionCode.POST_NOT_FOUND);
    }
}
