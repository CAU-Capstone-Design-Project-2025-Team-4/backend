package com.capstone2025.team4.backend.exception.matching;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class MatchingNotFound extends BusinessException {

    public MatchingNotFound() {
        super(ExceptionCode.MATCHING_NOT_FOUND);
    }
}
