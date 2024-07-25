package com.yuejiangw.usercenterbackend.service;

import com.yuejiangw.usercenterbackend.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试
 *
 * @author wu
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("user");
        user.setUserAccount("123");
        user.setAvatarUrl("www.test.com");
        user.setGender(0);
        user.setUserPassword("pwd");
        user.setPhone("123");
        user.setEmail("test@email.com");
        user.setUserStatus(0);

        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);
    }

    @Test
    void userRegister() {
        String userAccount ;
        String userPassword;
        String checkPassword;

        // 校验 userAccount 不为空
        userAccount = "";
        userPassword = "12345678";
        checkPassword = "12345678";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);

        // 校验 userAccount 长度不小于 4
        userAccount = "te";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);

        // 校验 userPassword 和 checkPassword 长度不小于 8
        userAccount = "test";
        userPassword = "1234567";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);

        // 校验 userAccount 不能包含特殊字符
        userAccount = "test 123";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);

        // 校验 userPassword 和 checkPassword 相等
        userAccount = "test";
        userPassword = "12345678";
        checkPassword = "12345679";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);
    }
}