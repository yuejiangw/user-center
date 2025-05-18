package com.yuejiangw.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.exception.CustomException;
import com.yuejiangw.usercenterbackend.model.entity.User;
import com.yuejiangw.usercenterbackend.service.UserService;
import com.yuejiangw.usercenterbackend.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yuejiangw.usercenterbackend.utils.UserUtils.isAdmin;
import static com.yuejiangw.usercenterbackend.common.constants.UserConstant.*;


/**
* @author yuejiangwu
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-05-13 15:31:15
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    private static final String SALT = "usercenter";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        return userRegister(userAccount, userPassword, checkPassword, null);
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, Integer userRole) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "userAccount, userPassword, checkPassword can not be null");
        }
        if (userAccount.length() < 4) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "userAccount's length should >= 4");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "userPassword's length should >= 8");
        }

        // 账户不能包含特殊字符
        String validPattern = "^[a-zA-Z0-9]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.matches()) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "You can only use digits and English letters to create your account");
        }

        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "Make sure the checkPassword is the same as the password you set");
        }

        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "This account has already been created, please login or use a different account name");
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. 插入数据
        final User.UserBuilder userBuilder = User.builder()
                .username(userAccount)  // By default, the user's init username will be the same as the user account
                .userAccount(userAccount)
                .userPassword(encryptPassword);

        if (userRole != null) {
            userBuilder.userRole(userRole);
        }

        final User user = userBuilder.build();

        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new CustomException(ErrorCode.SYSTEM_ERROR, "User can not be created");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "userAccount, userPassword, checkPassword can not be null");
        }
        if (userAccount.length() < 4) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "userAccount's length should >= 4");
        }
        if (userPassword.length() < 8) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "userPassword's length should >= 8");
        }

        // 账户不能包含特殊字符
        String validPattern = "^[a-zA-Z0-9]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.matches()) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "You can only use digits and English letters to create your account");
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);

        // 用户不存在
        if (user == null) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "Login failed, user doesn't exist or userAccount doesn't match userPassword");
        }

        // 3. 用户脱敏
        User safetyUser = desensitize(user);

        // 4. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    @Override
    public List<User> userSearch(Map<String, String> queryParams, HttpServletRequest request) {
        // TODO unit test
        // 仅管理员可查询
        if (!isAdmin(request)) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "only admin can search all users");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryParams.forEach((key, value) -> {
            if (USER_EXACT_MATCH.contains(key)) {
                queryWrapper.eq(key, value);
            }
            if (USER_PATTERN_MATCH.contains(key)) {
                queryWrapper.like(key, value);
            }
        });

        return userMapper.selectList(queryWrapper).stream().map(this::desensitize).toList();
    }

    @Override
    public User getUserById(long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new CustomException(ErrorCode.PARAMS_ERROR, "only admin can search all users");
        }
        return this.getById(id);
    }

    @Override
    public boolean deleteUser(long id, HttpServletRequest request) {
        // TODO unit test
        // 仅管理员可删除
        if (!isAdmin(request)) {
            throw new CustomException(ErrorCode.NO_AUTH, "Only Admin can delete a user");
        }

        final int rows = userMapper.deleteById(id);
        log.info("Delete {} rows", rows);
        return true;
    }

    @Override
    public Long createUser(String userAccount, Integer userRole, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new CustomException(ErrorCode.NO_AUTH, "Only Admin can create a user, normal user please use Register API");
        }

        return userRegister(userAccount, DEFAULT_PASSWORD, DEFAULT_PASSWORD, userRole);
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateById(user) > 0;
    }


    /**
     * 用户数据脱敏
     * @param user
     * @return
     */
    @Override
    public User desensitize(User user) {
        if (user == null) {
            return null;
        }

        return User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userAccount(user.getUserAccount())
                .avatarUrl(user.getAvatarUrl())
                .gender(user.getGender())
                .phone(user.getPhone())
                .email(user.getEmail())
                .userStatus(user.getUserStatus())
                .createTime(user.getCreateTime())
                .userRole(user.getUserRole())
                .build();
    }

    /**
     * 用户注销
     * @param request
     */
    @Override
    public Integer userLogout(HttpServletRequest request) {
        // 移除登录态
        try {
            request.getSession().removeAttribute(USER_LOGIN_STATE);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
