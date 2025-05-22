package com.yuejiangw.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.exception.BusinessException;
import com.yuejiangw.usercenterbackend.mapper.UserMapper;
import com.yuejiangw.usercenterbackend.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yuejiangw.usercenterbackend.common.constants.UserConstant.USER_LOGIN_STATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Spy
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getSession()).thenReturn(session);
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L); // 设置用户ID
            return true;
        }).when(userService).save(any(User.class));
    }

    @Test
    void userRegister_Success() {
        // 准备测试数据
        String userAccount = "testuser";
        String userPassword = "12345678";
        String checkPassword = "12345678";

        // 模拟数据库查询结果
        when(userMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);

        // 执行测试
        long userId = userService.userRegister(userAccount, userPassword, checkPassword);

        // 验证结果
        assertEquals(1L, userId);
        verify(userService).save(any(User.class));
    }

    @Test
    void userRegister_DuplicateAccount() {
        // 准备测试数据
        String userAccount = "testuser";
        String userPassword = "12345678";
        String checkPassword = "12345678";

        // 模拟数据库查询结果
        when(userMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.userRegister(userAccount, userPassword, checkPassword));

        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    @Test
    void userRegister_NullAccount() {
        // 准备测试数据
        String userAccount = null;
        String userPassword = "12345678";
        String checkPassword = "12345678";

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.userRegister(userAccount, userPassword, checkPassword));

        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    @Test
    void userRegister_EmptyAccount() {
        // 准备测试数据
        String userAccount = "";
        String userPassword = "12345678";
        String checkPassword = "12345678";

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.userRegister(userAccount, userPassword, checkPassword));

        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    @Test
    void userRegister_AccountTooShort() {
        // 准备测试数据
        String userAccount = "abc"; // 长度小于4
        String userPassword = "12345678";
        String checkPassword = "12345678";

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.userRegister(userAccount, userPassword, checkPassword));

        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    @Test
    void userRegister_PasswordTooShort() {
        // 准备测试数据
        String userAccount = "testuser";
        String userPassword = "1234567"; // 长度小于8
        String checkPassword = "1234567";

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.userRegister(userAccount, userPassword, checkPassword));

        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    @Test
    void userRegister_InvalidAccountCharacters() {
        // 准备测试数据
        String userAccount = "test@user"; // 包含特殊字符
        String userPassword = "12345678";
        String checkPassword = "12345678";

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.userRegister(userAccount, userPassword, checkPassword));

        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    @Test
    void userRegister_PasswordMismatch() {
        // 准备测试数据
        String userAccount = "testuser";
        String userPassword = "12345678";
        String checkPassword = "87654321"; // 与密码不匹配

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.userRegister(userAccount, userPassword, checkPassword));

        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    @Test
    void userRegister_WithUserRole() {
        // 准备测试数据
        String userAccount = "testuser";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        Integer userRole = 1; // 管理员角色

        // 模拟数据库查询结果
        when(userMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);

        // 执行测试
        long userId = userService.userRegister(userAccount, userPassword, checkPassword, userRole);

        // 验证结果
        assertEquals(1L, userId);
        verify(userService).save(any(User.class));
    }

    @Test
    void userLogin_Success() {
        // 准备测试数据
        String userAccount = "testuser";
        String userPassword = "12345678";
        User mockUser = User.builder()
                .id(1L)
                .userAccount(userAccount)
                .userPassword("e5d0c7c8b7a6f5e4d3c2b1a0f9e8d7c6") // 加密后的密码
                .build();

        // 模拟数据库查询结果
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(mockUser);

        // 执行测试
        User result = userService.userLogin(userAccount, userPassword, request);

        // 验证结果
        assertNotNull(result);
        assertEquals(userAccount, result.getUserAccount());
        verify(session).setAttribute(eq(USER_LOGIN_STATE), any(User.class));
    }

    @Test
    void userLogin_WrongPassword() {
        // 准备测试数据
        String userAccount = "testuser";
        String userPassword = "wrongpassword";

        // 模拟数据库查询结果
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.userLogin(userAccount, userPassword, request));

        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    @Test
    void userSearch_AdminSuccess() {
        // 准备测试数据
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("username", "test");
        User mockUser = User.builder()
                .id(1L)
                .username("testuser")
                .build();

        // 模拟管理员权限
        when(request.getSession().getAttribute(USER_LOGIN_STATE)).thenReturn(
                User.builder().userRole(1).build());
        when(userMapper.selectList(any(QueryWrapper.class))).thenReturn(List.of(mockUser));

        // 执行测试
        List<User> result = userService.userSearch(queryParams, request);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    void deleteUser_AdminSuccess() {
        // 准备测试数据
        long userId = 1L;

        // 模拟管理员权限
        when(request.getSession().getAttribute(USER_LOGIN_STATE)).thenReturn(
                User.builder().userRole(1).build());
        when(userMapper.deleteById(userId)).thenReturn(1);

        // 执行测试
        boolean result = userService.deleteUser(userId, request);

        // 验证结果
        assertTrue(result);
        verify(userMapper).deleteById(userId);
    }

    @Test
    void userLogout_Success() {
        // 执行测试
        Integer result = userService.userLogout(request);

        // 验证结果
        assertEquals(1, result);
        verify(session).removeAttribute(USER_LOGIN_STATE);
    }

    @Test
    void desensitize_Success() {
        // 准备测试数据
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .userAccount("test")
                .userPassword("password")
                .build();

        // 执行测试
        User result = userService.desensitize(user);

        // 验证结果
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getUserAccount(), result.getUserAccount());
        assertNull(result.getUserPassword()); // 密码应该被脱敏
    }
}