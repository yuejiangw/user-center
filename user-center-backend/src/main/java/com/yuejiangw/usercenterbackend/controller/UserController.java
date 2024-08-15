package com.yuejiangw.usercenterbackend.controller;

import com.yuejiangw.usercenterbackend.model.domain.User;
import com.yuejiangw.usercenterbackend.model.request.UserLoginRequest;
import com.yuejiangw.usercenterbackend.model.request.UserRegisterRequest;
import com.yuejiangw.usercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.yuejiangw.usercenterbackend.constant.UserConstant.USER_LOGIN_STATE;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody final UserRegisterRequest request) {
        if (request == null) {
            return null;
        }

        final String userAccount = request.getUserAccount();
        final String userPassword = request.getUserPassword();
        final String checkPassword = request.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @GetMapping("/current")
    public User getCurrentUser(final HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        // 尽管已经获取了登录信息但还是建议从数据库中取数据，因为用户信息可能会变化但 session 中的信息不一定会变，以数据库为准
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        return userService.desensitize(user);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody final UserLoginRequest request, final HttpServletRequest httpServletRequest) {
        if (request == null) {
            return null;
        }

        final String userAccount = request.getUserAccount();
        final String userPassword = request.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        return userService.userLogin(userAccount, userPassword, httpServletRequest);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        return userService.userSearch(username, request);
    }

    @PostMapping("/delete")
    public boolean deleteUser(long id, HttpServletRequest request) {
        return userService.deleteUser(id, request);
    }
}
