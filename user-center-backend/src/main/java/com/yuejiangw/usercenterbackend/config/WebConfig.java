package com.yuejiangw.usercenterbackend.config;

import com.yuejiangw.usercenterbackend.interceptor.JwtInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/user/**") // 需要认证的路径
                .excludePathPatterns(
                        "/api/user/register", // 注册接口
                        "/api/user/login", // 登录接口
                        "/api/user/logout" // 登出接口
                );
    }
}