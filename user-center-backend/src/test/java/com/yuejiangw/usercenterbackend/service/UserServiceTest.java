package com.yuejiangw.usercenterbackend.service;

import com.yuejiangw.usercenterbackend.exception.CustomException;
import com.yuejiangw.usercenterbackend.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.yuejiangw.usercenterbackend.service.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试
 *
 * @author Wu
 */
@SpringBootTest
@Transactional
class UserServiceTest {

    @Resource
    UserService userService;

    @Test
    // @Rollback(value = false)
    void testAddUser_successful() {
        User user = new User();
        user.setUsername("username123");
        user.setUserAccount("account123");
        user.setAvatarUrl("www.avatar.com");
        user.setGender(0);
        user.setUserPassword("password");
        user.setPhone("123");
        user.setEmail("test@email.com");
        user.setUserStatus(0);

        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);
    }

    @Test
    void testUserRegister_successful() {
        long userId = userService.userRegister(VALID_ACCOUNT, VALID_PASSWORD, VALID_CHECK_PASSWORD);
        assertNotEquals(-1, userId);
    }

    @Test
    void testUserRegister_fail() {
        // 校验 userAccount 不为空
        assertThrows(CustomException.class, () -> {
            userService.userRegister(INVALID_ACCOUNT_EMPTY, VALID_PASSWORD, VALID_CHECK_PASSWORD);
        });

        // 校验 userAccount 长度不小于 4
        assertThrows(CustomException.class, () -> {
            userService.userRegister(INVALID_ACCOUNT_LENGTH, VALID_PASSWORD, VALID_CHECK_PASSWORD);
        });

        // 校验 userAccount 不能包含特殊字符
        assertThrows(CustomException.class, () -> {
            userService.userRegister(INVALID_ACCOUNT_CHAR, VALID_PASSWORD, VALID_CHECK_PASSWORD);
        });

        // 校验 userPassword 和 checkPassword 长度不小于 8
        assertThrows(CustomException.class, () -> {
            userService.userRegister(VALID_ACCOUNT, INVALID_PASSWORD_LENGTH, INVALID_CHECK_PASSWORD_LENGTH);
        });

        // 校验 userPassword 和 checkPassword 相等
        assertThrows(CustomException.class, () -> {
            userService.userRegister(VALID_ACCOUNT, VALID_PASSWORD, INVALID_CHECK_PASSWORD_LENGTH);
        });
    }
}