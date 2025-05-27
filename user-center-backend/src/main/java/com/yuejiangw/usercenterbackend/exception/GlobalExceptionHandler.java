package com.yuejiangw.usercenterbackend.exception;

import com.yuejiangw.usercenterbackend.common.BaseResponse;
import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> customExceptionHandler(BusinessException e) {
        log.error("Business Exception: {}. Description: {}", e.getMessage(), e.getMessage());
        return ResponseUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> customExceptionHandler(RuntimeException e) {
        log.error("Runtime Exception: {}", e.getMessage());
        return ResponseUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
