package com.yuejiangw.usercenterbackend.controller;

import com.yuejiangw.usercenterbackend.common.BaseResponse;
import com.yuejiangw.usercenterbackend.exception.BusinessException;
import com.yuejiangw.usercenterbackend.model.dto.UserLoginRequest;
import com.yuejiangw.usercenterbackend.model.dto.UserRegisterRequest;
import com.yuejiangw.usercenterbackend.model.dto.UserUpdateRequest;
import com.yuejiangw.usercenterbackend.model.entity.User;
import com.yuejiangw.usercenterbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void userRegister_Success() {
        // 准备测试数据
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUserAccount("testuser");
        registerRequest.setUserPassword("12345678");
        registerRequest.setCheckPassword("12345678");

        when(userService.userRegister(anyString(), anyString(), anyString())).thenReturn(1L);

        // 执行测试
        BaseResponse<Long> response = userController.userRegister(registerRequest);

        // 验证结果
        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals(1L, response.getData());
    }

    @Test
    void userRegister_NullRequest() {
        // 执行测试并验证异常
        assertThrows(BusinessException.class, () -> userController.userRegister(null));
    }

    @Test
    void userLogin_Success() {
        // 准备测试数据
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUserAccount("testuser");
        loginRequest.setUserPassword("12345678");

        User mockUser = User.builder()
                .id(1L)
                .userAccount("testuser")
                .build();

        when(userService.userLogin(anyString(), anyString(), any(HttpServletRequest.class)))
                .thenReturn(mockUser);

        // 执行测试
        BaseResponse<User> response = userController.userLogin(loginRequest, request);

        // 验证结果
        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals("testuser", response.getData().getUserAccount());
    }

    @Test
    void userLogin_NullRequest() {
        // 执行测试
        BaseResponse<User> response = userController.userLogin(null, request);

        // 验证结果
        assertNull(response);
    }

    @Test
    void currentUser_Success() {
        // 准备测试数据
        User mockUser = User.builder()
                .id(1L)
                .userAccount("testuser")
                .build();

        when(request.getSession().getAttribute(anyString())).thenReturn(mockUser);
        when(userService.getById(anyLong())).thenReturn(mockUser);
        when(userService.desensitize(any(User.class))).thenReturn(mockUser);

        // 执行测试
        BaseResponse<User> response = userController.currentUser(request);

        // 验证结果
        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals("testuser", response.getData().getUserAccount());
    }

    @Test
    void userLogout_Success() {
        // 模拟 userService.userLogout 返回 1
        when(userService.userLogout(any(HttpServletRequest.class))).thenReturn(1);

        // 执行测试
        BaseResponse<Integer> response = userController.userLogout(request);

        // 验证结果
        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals(1, response.getData());
    }

    @Test
    void searchUsers_Success() {
        // 准备测试数据
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("username", "test");

        List<User> mockUsers = List.of(
                User.builder().id(1L).username("testuser").build());

        when(userService.userSearch(anyMap(), any(HttpServletRequest.class)))
                .thenReturn(mockUsers);

        // 执行测试
        BaseResponse<List<User>> response = userController.searchUsers(queryParams, request);

        // 验证结果
        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals(1, response.getData().size());
        assertEquals("testuser", response.getData().get(0).getUsername());
    }

    @Test
    void getUserById_Success() {
        // 准备测试数据
        User mockUser = User.builder()
                .id(1L)
                .userAccount("testuser")
                .build();

        when(userService.getUserById(anyLong(), any(HttpServletRequest.class)))
                .thenReturn(mockUser);

        // 执行测试
        BaseResponse<User> response = userController.getUserById(1L, request);

        // 验证结果
        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals("testuser", response.getData().getUserAccount());
    }

    @Test
    void deleteUser_Success() {
        // mock userService.deleteUser 返回 true
        when(userService.deleteUser(anyLong(), any(HttpServletRequest.class))).thenReturn(true);

        // 执行测试
        BaseResponse<Boolean> response = userController.deleteUser(1L, request);

        // 验证结果
        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertTrue(response.getData());
    }

    @Test
    void createUser_Success() {
        // 执行测试
        BaseResponse<Long> response = userController.createUser("testuser", 0, request);

        // 验证结果
        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertNotNull(response.getData());
    }

    @Test
    void updateUser_Success() {
        // 准备测试数据
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setId(1L);
        updateRequest.setUsername("newusername");
        updateRequest.setUserAccount("testuser");
        updateRequest.setAvatarUrl("http://example.com/avatar.jpg");
        updateRequest.setGender(1);
        updateRequest.setPhone("1234567890");
        updateRequest.setEmail("test@example.com");

        when(userService.updateUser(any(User.class), any(HttpServletRequest.class))).thenReturn(true);

        // 执行测试
        BaseResponse<Boolean> response = userController.updateUser(updateRequest, request);

        // 验证结果
        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertTrue(response.getData());
    }

    @Test
    void updateUser_NullRequest() {
        // 执行测试并验证异常
        assertThrows(BusinessException.class, () -> userController.updateUser(null, null));
    }
}