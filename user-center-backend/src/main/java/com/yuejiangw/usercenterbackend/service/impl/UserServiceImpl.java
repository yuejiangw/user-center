package com.yuejiangw.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.exception.BusinessException;
import com.yuejiangw.usercenterbackend.model.entity.User;
import com.yuejiangw.usercenterbackend.service.UserService;
import com.yuejiangw.usercenterbackend.mapper.UserMapper;
import com.yuejiangw.usercenterbackend.service.RedisSessionService;
import com.yuejiangw.usercenterbackend.utils.JwtUtils;
import com.yuejiangw.usercenterbackend.utils.UserUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.yuejiangw.usercenterbackend.utils.UserUtils.isAdmin;
import static com.yuejiangw.usercenterbackend.utils.UserUtils.getCurrentUser;
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

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisSessionService redisSessionService;

    private static final String SALT = "usercenter";

    /**
     * 该方法是用户注册的重载方法，不带 userRole 参数。
     * 当调用此方法时，实际上会调用带有 userRole 参数的 userRegister 方法，并将 userRole 设为 null，
     * 也就是默认不指定用户角色，通常代表注册普通用户。
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        return userRegister(userAccount, userPassword, checkPassword, null);
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, Integer userRole) {
        // 1. 校验
        UserUtils.checkNull(userAccount, userPassword, checkPassword);
        UserUtils.checkLength(userAccount, userPassword, checkPassword);
        UserUtils.checkValidCharacter(userAccount);

        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,
                    "Make sure the checkPassword is the same as the password you set");
        }

        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,
                    "This account has already been created, please login or use a different account name");
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. 插入数据
        final User.UserBuilder userBuilder = User.builder()
                .username(userAccount) // By default, the user's init username will be the same as the user account
                .userAccount(userAccount)
                .userPassword(encryptPassword);

        if (userRole != null) {
            userBuilder.userRole(userRole);
        }

        final User user = userBuilder.build();

        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "User can not be created");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        UserUtils.checkNull(userAccount, userPassword);
        UserUtils.checkLength(userAccount, userPassword);
        UserUtils.checkValidCharacter(userAccount);

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);

        // 用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,
                    "Login failed, user doesn't exist or userAccount doesn't match userPassword");
        }

        // 3. 用户脱敏
        User safetyUser = desensitize(user);

        // 4. 生成 JWT token
        String token = jwtUtils.generateToken(safetyUser);

        // 5. 将用户信息存储到 Redis
        redisSessionService.storeUserSession(token, safetyUser);

        // 6. 将 token 存储到 request 属性中，供 Controller 返回
        request.setAttribute("token", token);

        return safetyUser;
    }

    @Override
    public List<User> userSearch(Map<String, String> queryParams, HttpServletRequest request) {
        // 仅管理员可查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "only admin can search all users");
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "only admin can search all users");
        }
        return this.getById(id);
    }

    @Override
    public boolean deleteUser(long id, HttpServletRequest request) {
        // 仅管理员可删除
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "Only Admin can delete a user");
        }

        final int rows = userMapper.deleteById(id);
        log.info("Delete {} rows", rows);
        return true;
    }

    @Override
    public Long createUser(String userAccount, Integer userRole, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH,
                    "Only Admin can create a user, normal user please use Register API");
        }

        return userRegister(userAccount, DEFAULT_PASSWORD, DEFAULT_PASSWORD, userRole);
    }

    @Override
    public boolean updateUser(User user, HttpServletRequest httpServletRequest) {
        // 从 request 属性中获取当前用户
        User currentUser = (User) httpServletRequest.getAttribute("currentUser");
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "User not logged in");
        }

        // 只有管理员或用户本人可以更新用户信息
        if (!isAdmin(httpServletRequest) && !currentUser.getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "Only Admin can update other user's information.");
        }

        return userMapper.updateById(user) > 0;
    }

    /**
     * 用户数据脱敏
     * 
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
     * 
     * @param request
     */
    @Override
    public Integer userLogout(HttpServletRequest request) {
        // 从请求头获取 token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // 从 Redis 中移除用户会话
            redisSessionService.removeUserSession(token);
            return 1;
        }
        return 0;
    }
}
