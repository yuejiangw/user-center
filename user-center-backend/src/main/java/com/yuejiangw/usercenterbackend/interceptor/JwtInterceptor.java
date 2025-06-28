package com.yuejiangw.usercenterbackend.interceptor;

import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.exception.BusinessException;
import com.yuejiangw.usercenterbackend.model.entity.User;
import com.yuejiangw.usercenterbackend.service.RedisSessionService;
import com.yuejiangw.usercenterbackend.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisSessionService redisSessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取请求头中的 Authorization
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "Please provide valid authorization token");
        }

        // 提取 token
        String token = authHeader.substring(7);

        try {
            // 验证 token
            if (!redisSessionService.isSessionExists(token)) {
                throw new BusinessException(ErrorCode.NOT_LOGIN, "Session expired or invalid");
            }

            // 从 Redis 获取用户信息
            User user = redisSessionService.getUserSession(token);
            if (user == null) {
                throw new BusinessException(ErrorCode.NOT_LOGIN, "User session not found");
            }

            // 验证 token 是否与用户匹配
            if (!jwtUtils.validateToken(token, user.getUserAccount())) {
                throw new BusinessException(ErrorCode.NOT_LOGIN, "Invalid token");
            }

            // 将用户信息存储到 request 属性中，供后续使用
            request.setAttribute("currentUser", user);

            // 刷新会话过期时间
            redisSessionService.refreshSession(token);

            return true;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "Token validation failed");
        }
    }
}