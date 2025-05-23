package com.yuejiangw.usercenterbackend.service;

import com.yuejiangw.usercenterbackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
* 用户服务
* @author yuejiangwu
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-05-13 15:31:15
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    long userRegister(String userAccount, String userPassword, String checkPassword, Integer userRole);

    /**
     * 用户登录
     * @param userAccount 账号
     * @param userPassword 密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    List<User> userSearch(Map<String, String> queryParams, HttpServletRequest request);

    User getUserById(long id, HttpServletRequest request);

    boolean deleteUser(final long id, HttpServletRequest request);

    Long createUser(String userAccount, Integer userRole, HttpServletRequest request);

    boolean updateUser(User user, HttpServletRequest httpServletRequest);

    User desensitize(User user);

    Integer userLogout(HttpServletRequest request);
}
