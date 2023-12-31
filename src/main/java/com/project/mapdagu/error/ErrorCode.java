package com.project.mapdagu.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.springframework.http.HttpStatus.*;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_ALLOWED_MEMBER(FORBIDDEN, "해당 요청에 대한 권한이 없습니다."),
    NOT_AUTHENTICATED_REQUEST(SC_UNAUTHORIZED, "유효한 JWT 토큰이 없습니다."),

    TOKEN_NOT_EXIST(NOT_FOUND, "토큰이 존재하지 않습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "잘못된 토큰입니다."),

    MEMBER_NOT_FOUND(NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    FOOD_NOT_FOUND(NOT_FOUND, "해당 음식을 찾을 수 없습니다."),
    EVALUATION_NOT_FOUND(NOT_FOUND, "해당 평가를 찾을 수 없습니다."),
    FRIEND_REQUEST_NOT_FOUND(NOT_FOUND, "해당 친구 요청을 찾을 수 없습니다."),
    FRIEND_NOT_FOUND(NOT_FOUND, "해당 회원과 친구가 아닙니다."),


    INVALID_FILE_UPLOAD(BAD_REQUEST, "파일 업로드에 실패하였습니다."),
    WRONG_SEARCH(BAD_REQUEST, "검색어를 입력해야 합니다."),
    ALREADY_EXIST_EVALUATION(BAD_REQUEST, "이미 존재하는 평가입니다."),
    ALREADY_LOGOUT_MEMBER(BAD_REQUEST, "이미 로그아웃한 회원입니다"),
    EMAIL_SEND_ERROR(BAD_REQUEST, "이메일 인증 코드 전송을 실패했습니다."),
    ALREADY_EXIST_EMAIL(BAD_REQUEST, "이미 존재하는 이메일입니다."),
    ALREADY_EXIST_USERNAME(BAD_REQUEST, "이미 존재하는 사용자 이름입니다."),
    ALREADY_EXIST_FRIEND(BAD_REQUEST, "이미 상대방과 친구입니다."),
    ALREADY_EXIST_FRIEND_REQUEST(BAD_REQUEST, "이미 보낸 친구 요청입니다.");

    private final int code;
    private final String message;

    ErrorCode(HttpStatus code, String message) {
        this.code = code.value();
        this.message = message;
    }
}
