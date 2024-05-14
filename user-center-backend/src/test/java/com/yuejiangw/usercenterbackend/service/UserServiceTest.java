package com.yuejiangw.usercenterbackend.service;
import java.util.Date;

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
}