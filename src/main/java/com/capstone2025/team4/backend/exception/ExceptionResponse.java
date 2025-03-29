package com.capstone2025.team4.backend.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExceptionResponse {
    private List<String> messages = new ArrayList<>();

    public ExceptionResponse(ExceptionCode exceptionCode) {
        this.messages.add(exceptionCode.getMessage());
    }

    public ExceptionResponse(List<String> msgs) {
        this.messages = msgs;
    }
}
