package com.yuejiangw.usercenterbackend.controller;

import com.yuejiangw.usercenterbackend.model.domain.User;
import com.yuejiangw.usercenterbackend.model.request.UserLoginRequest;
import com.yuejiangw.usercenterbackend.model.request.UserRegisterRequest;
import com.yuejiangw.usercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
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
