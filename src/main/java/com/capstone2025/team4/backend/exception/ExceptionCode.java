package com.capstone2025.team4.backend.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ExceptionCode {
    USER_EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다"),
    USER_PASSWORD_INCONSISTENT(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    USER_BAD_REQUEST(HttpStatus.BAD_REQUEST, "입력하신 정보를 다시 확인해주세요"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 사용자를 찾지 못했습니다")

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
