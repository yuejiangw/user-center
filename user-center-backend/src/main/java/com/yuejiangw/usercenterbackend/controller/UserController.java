package com.yuejiangw.usercenterbackend.controller;

import com.yuejiangw.usercenterbackend.common.BaseResponse;
import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.common.ResponseUtils;
import com.yuejiangw.usercenterbackend.exception.CustomException;
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

import static com.yuejiangw.usercenterbackend.common.UserConstant.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody final UserRegisterRequest request) {
        if (request == null) {
//            return ResponseUtils.error(ErrorCode.PARAMS_ERROR);
            throw new CustomException(ErrorCode.PARAMS_ERROR);
        }

        final String userAccount = request.getUserAccount();
        final String userPassword = request.getUserPassword();
        final String checkPassword = request.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        return ResponseUtils.success(userService.userRegister(userAccount, userPassword, checkPassword));
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(final HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        // 尽管已经获取了登录信息但还是建议从数据库中取数据，因为用户信息可能会变化但 session 中的信息不一定会变，以数据库为准
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        final User user = userService.getById(userId);
        return ResponseUtils.success(userService.desensitize(user));
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody final UserLoginRequest request, final HttpServletRequest httpServletRequest) {
        if (request == null) {
            return null;
        }

        final String userAccount = request.getUserAccount();
        final String userPassword = request.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        return ResponseUtils.success(userService.userLogin(userAccount, userPassword, httpServletRequest));
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(final HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return ResponseUtils.success(userService.userLogout(request));
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(final String username, final HttpServletRequest request) {
        return ResponseUtils.success(userService.userSearch(username, request));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(final long id, final HttpServletRequest request) {
        return ResponseUtils.success(userService.deleteUser(id, request));
    }
}
