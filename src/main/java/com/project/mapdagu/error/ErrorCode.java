package com.project.mapdagu.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    TOKEN_NOT_EXIST(NOT_FOUND, "토큰이 존재하지 않습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "잘못된 토큰입니다."),

    ALREADY_EXIST_EMAIL(BAD_REQUEST, "이미 존재하는 이메일입니다."),
    ALREADY_EXIST_USERNAME(BAD_REQUEST, "이미 존재하는 사용자 이름입니다.");

    private final int code;
    private final String message;

    ErrorCode(HttpStatus code, String message) {
        this.code = code.value();
        this.message = message;
    }
}
