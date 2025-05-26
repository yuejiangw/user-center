package com.yuejiangw.usercenterbackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    PARAMS_ERROR(40000, "request params error"),
    NULL_ERROR(40001, "request data is null"),
    NOT_LOGIN(40100, "not login"),
    SYSTEM_ERROR(50000, "system internal error"),
    NO_AUTH(40101, "not authenticated");

    private final int code;

    private final String message;
}
