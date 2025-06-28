package com.yuejiangw.usercenterbackend.service;

import com.yuejiangw.usercenterbackend.model.entity.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisSessionService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String SESSION_PREFIX = "user:session:";
    private static final String TOKEN_PREFIX = "user:token:";
    private static final long SESSION_EXPIRE_TIME = 24 * 60 * 60; // 24 hours in seconds

    /**
     * Store user session in Redis
     * 
     * @param token JWT token
     * @param user  User object
     */
    public void storeUserSession(String token, User user) {
        String sessionKey = SESSION_PREFIX + token;
        String tokenKey = TOKEN_PREFIX + user.getId();

        // Store user session
        redisTemplate.opsForValue().set(sessionKey, user, SESSION_EXPIRE_TIME, TimeUnit.SECONDS);

        // Store token mapping to user ID for logout functionality
        redisTemplate.opsForValue().set(tokenKey, token, SESSION_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * Get user session from Redis
     * 
     * @param token JWT token
     * @return User object or null if not found
     */
    public User getUserSession(String token) {
        String sessionKey = SESSION_PREFIX + token;
        Object userObj = redisTemplate.opsForValue().get(sessionKey);

        if (userObj instanceof User) {
            return (User) userObj;
        }
        return null;
    }

    /**
     * Remove user session from Redis
     * 
     * @param token JWT token
     */
    public void removeUserSession(String token) {
        String sessionKey = SESSION_PREFIX + token;
        redisTemplate.delete(sessionKey);
    }

    /**
     * Remove user session by user ID
     * 
     * @param userId User ID
     */
    public void removeUserSessionByUserId(Long userId) {
        String tokenKey = TOKEN_PREFIX + userId;
        String token = (String) redisTemplate.opsForValue().get(tokenKey);

        if (token != null) {
            removeUserSession(token);
            redisTemplate.delete(tokenKey);
        }
    }

    /**
     * Check if user session exists
     * 
     * @param token JWT token
     * @return true if exists, false otherwise
     */
    public boolean isSessionExists(String token) {
        String sessionKey = SESSION_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(sessionKey));
    }

    /**
     * Refresh session expiration time
     * 
     * @param token JWT token
     */
    public void refreshSession(String token) {
        String sessionKey = SESSION_PREFIX + token;
        String tokenKey = TOKEN_PREFIX + getUserSession(token).getId();

        redisTemplate.expire(sessionKey, SESSION_EXPIRE_TIME, TimeUnit.SECONDS);
        redisTemplate.expire(tokenKey, SESSION_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * Get session expiration time
     * 
     * @param token JWT token
     * @return Expiration time in seconds, -1 if not found
     */
    public long getSessionExpiration(String token) {
        String sessionKey = SESSION_PREFIX + token;
        Long expireTime = redisTemplate.getExpire(sessionKey, TimeUnit.SECONDS);
        return expireTime != null ? expireTime : -1;
    }
}