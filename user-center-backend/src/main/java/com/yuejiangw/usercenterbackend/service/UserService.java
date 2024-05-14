package com.yuejiangw.usercenterbackend.service;

import com.yuejiangw.usercenterbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 用户服务
* @author yuejiangwu
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-05-13 15:31:15
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param username 用户名
     * @param userAccount 账号
     * @param checkPassword 密码
     * @return 用户id
     */
    long userRegister(String username, String userAccount, String checkPassword);
}
