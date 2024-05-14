package com.yuejiangw.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuejiangw.usercenterbackend.model.domain.User;
import com.yuejiangw.usercenterbackend.service.UserService;
import com.yuejiangw.usercenterbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author yuejiangwu
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-05-13 15:31:15
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public long userRegister(String username, String userAccount, String checkPassword) {
        // 1. 校验
        if (userAccount == null || userAccount.length() == 0) {
            return 0;
        }
        return 1;
    }
}




