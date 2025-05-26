package com.yuejiangw.usercenterbackend.exception;

import com.yuejiangw.usercenterbackend.common.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    private final String message;

    public BusinessException(final String message, final int code, final String description) {
        super(message);
        this.code = code;
        this.message = description;
    }

    public BusinessException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public BusinessException(final ErrorCode errorCode, final String message) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = message;
    }
}
