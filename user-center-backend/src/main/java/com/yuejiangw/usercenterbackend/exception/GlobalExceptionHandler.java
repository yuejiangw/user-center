package com.yuejiangw.usercenterbackend.exception;

import com.yuejiangw.usercenterbackend.common.BaseResponse;
import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.common.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public BaseResponse customExceptionHandler(CustomException e) {
        log.error("customException: {}", e.getMessage());
        return ResponseUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse customExceptionHandler(RuntimeException e) {
        log.error("runtimeException: {}", e.getMessage());
        return ResponseUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
