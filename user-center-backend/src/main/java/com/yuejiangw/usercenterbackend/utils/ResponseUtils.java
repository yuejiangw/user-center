package com.yuejiangw.usercenterbackend.utils;

import com.yuejiangw.usercenterbackend.common.BaseResponse;
import com.yuejiangw.usercenterbackend.common.ErrorCode;


public class ResponseUtils {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, null, data, null);
    }

    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode, message);
    }
}
