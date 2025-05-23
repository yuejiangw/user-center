package com.yuejiangw.usercenterbackend.utils;

import com.yuejiangw.usercenterbackend.common.BaseResponse;
import com.yuejiangw.usercenterbackend.common.ErrorCode;

import static com.yuejiangw.usercenterbackend.common.constants.UserConstant.CODE_OK;
import static com.yuejiangw.usercenterbackend.common.constants.UserConstant.MESSAGE_OK;

public class ResponseUtils {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(CODE_OK, data, MESSAGE_OK);
    }

    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode, message);
    }

    public static BaseResponse<?> error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode, message, description);
    }
}
