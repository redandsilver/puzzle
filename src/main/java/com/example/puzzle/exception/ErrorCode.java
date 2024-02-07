package com.example.puzzle.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    ALREADY_EXIST_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "없는 회원입니다."),
    WRONG_LOGIN_INFO(HttpStatus.BAD_REQUEST, "잘못된 로그인 정보입니다."),
    NOT_VERIFIED_USER(HttpStatus.BAD_REQUEST, "인증되지 않은 회원입니다."),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "토큰이 없습니다."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "다시 로그인 해주세요."),
    ALREADY_LOGOUT_USER(HttpStatus.BAD_REQUEST, "이미 로그아웃한 회원입니다."),
    SEND_SMS_FAIL(HttpStatus.BAD_REQUEST, "발송 실패"),
    WRONG_CODE(HttpStatus.BAD_REQUEST, "인증코드가 다릅니다."),
    EXPIRED_CODE(HttpStatus.BAD_REQUEST, "유효시간이 지났습니다."),
    NOT_EXIST_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "휴대폰 번호를 확인 해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
