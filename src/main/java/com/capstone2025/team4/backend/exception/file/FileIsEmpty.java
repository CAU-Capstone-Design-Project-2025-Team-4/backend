package com.capstone2025.team4.backend.exception.file;

import com.capstone2025.team4.backend.exception.BusinessException;
import com.capstone2025.team4.backend.exception.ExceptionCode;

public class FileIsEmpty extends BusinessException {

    public FileIsEmpty() {
        super(ExceptionCode.FILE_IS_EMPTY);
    }
}
