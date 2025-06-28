package com.yuejiangw.usercenterbackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 通用返回类
 * 
 * @param <T>
 */
@Getter
@AllArgsConstructor
public class BaseResponse<T> implements Serializable {
    private final boolean success;

    private final Integer errorCode;

    private final T data;

    private final String errorMessage;

    public BaseResponse(ErrorCode errorCode, String errorMessage) {
        this(false, errorCode.getCode(), null, errorMessage);
    }

    public BaseResponse(ErrorCode errorCode) {
        this(false, errorCode.getCode(), null, errorCode.getMessage());
    }
}
