package com.yuejiangw.usercenterbackend.exception;

import com.yuejiangw.usercenterbackend.common.ErrorCode;

public class CustomException extends RuntimeException {
    private final int code;

    private final String description;

    public CustomException(final String message, final int code, final String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public CustomException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public CustomException(final ErrorCode errorCode, final String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
