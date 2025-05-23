package com.yuejiangw.usercenterbackend.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(20000, "ok", ""),
    PARAMS_ERROR(40000, "request params error", ""),
    NULL_ERROR(40001, "request data is null", ""),
    NOT_LOGIN(40100, "not login", ""),
    SYSTEM_ERROR(50000, "system internal error", ""),
    NO_AUTH(40101, "not authenticated", "");

    private final int code;

    private final String message;

    private final String description;

    ErrorCode(final int code, final String message, final String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
