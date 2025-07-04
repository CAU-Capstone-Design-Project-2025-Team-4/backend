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
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 사용자를 찾지 못했습니다"),
    USER_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "해당 사용자는 권한이 없습니다"),

    DESIGN_SOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "템플릿 디자인이 존재하지 않습니다"),
    DESIGN_NOT_FOUND(HttpStatus.NOT_FOUND, "디자인이 존재하지 않습니다"),
    DESIGN_NOT_SHARED(HttpStatus.FORBIDDEN, "해당 디자인은 공유되지 않았습니다"),
    DESIGN_ALREADY_HAS_POST(HttpStatus.CONFLICT, "해당 디자인에 대한 게시글이 이미 존재합니다"),

    ELEMENT_DEFAULT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 기본 요소는 없는 요소입니다."),
    ELEMENT_NOT_DEFAULT(HttpStatus.EXPECTATION_FAILED, "해당 요소는 기본 요소가 아닙니다"),
    SLIDE_ELEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "슬라이드의 요소가 존재하지 않습니다"),
    ELEMENT_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "저장된 파일을 찾을 수 없습니다"),
    ELEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 요소입니다"),
    ELEMENT_NOT_FILE(HttpStatus.BAD_REQUEST, "해당 요소 타입은 허용되지 않습니다 (허용 : 이미지, 모델)"),
    ELEMENT_NOT_TEXT(HttpStatus.BAD_REQUEST, "해당 요소 타입은 허용되지 않습니다 (허용 : 텍스트, svg)"),
    ELEMENT_NOT_SPATIAL(HttpStatus.BAD_REQUEST, "해당 요소는 spatial type이 아닙니다"),

    FILE_IS_EMPTY(HttpStatus.BAD_REQUEST, "빈 파일입니다"),

    SLIDE_NOT_FOUND(HttpStatus.NOT_FOUND, "슬라이드를 찾을 수 없습니다"),
    SLIDE_ORDER_DUPLICATE(HttpStatus.CONFLICT, "슬라이드 순서 중복이 일어났습니다"),

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스트를 찾을 수 없습니다"),

    MATCHING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 매칭을 찾을 수 없습니다"),

    MODEL_NOT_FOUNT(HttpStatus.NOT_FOUND, "해당 모델을 찾을 수 없습니다"),

    POST_TITLE_EMPTY(HttpStatus.BAD_REQUEST, "제목이 비어있습니다"),

    TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "템필릿을 찾을 수 없습니다"),

    ANIMATION_NOT_FOUND(HttpStatus.NOT_FOUND, "애니메이션을 찾을 수 없습니다"),

    FRAME_NOT_FOUND(HttpStatus.NOT_FOUND, "프레임을 찾을 수 없습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
