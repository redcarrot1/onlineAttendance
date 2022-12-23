package com.sungam1004.register.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "U001", "해당 사용자를 찾을 수 없습니다."),
    NOT_FORMAT_MATCH_USER_PASSWORD(HttpStatus.BAD_REQUEST, "P001", "비밀번호는 숫자 4자리여야 합니다."),
    NOT_FORMAT_MATCH_ADMIN_PASSWORD(HttpStatus.BAD_REQUEST, "P002", "비밀번호는 5자리 이상, 20자리 이하여야 합니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "P003", "비밀번호가 일치하지 않습니다."),


    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}
