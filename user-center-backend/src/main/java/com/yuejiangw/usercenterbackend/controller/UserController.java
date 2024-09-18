package com.yuejiangw.usercenterbackend.controller;

import com.yuejiangw.usercenterbackend.common.BaseResponse;
import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.common.ResponseUtils;
import com.yuejiangw.usercenterbackend.common.UserUtils;
import com.yuejiangw.usercenterbackend.exception.CustomException;
import com.yuejiangw.usercenterbackend.model.domain.User;
import com.yuejiangw.usercenterbackend.model.request.UserLoginRequest;
import com.yuejiangw.usercenterbackend.model.request.UserRegisterRequest;
import com.yuejiangw.usercenterbackend.model.request.UserUpdateRequest;
import com.yuejiangw.usercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody final UserRegisterRequest request) {
        if (request == null) {
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
    public BaseResponse<User> currentUser(final HttpServletRequest request) {
        User currentUser = UserUtils.getCurrentUser(request);

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
    public BaseResponse<List<User>> searchUsers(@RequestParam Map<String, String> queryParams, final HttpServletRequest request) {
        log.info(queryParams.toString());
        return ResponseUtils.success(userService.userSearch(queryParams, request));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestParam final long id, final HttpServletRequest request) {
        return ResponseUtils.success(userService.deleteUser(id, request));
    }

    @PostMapping("/create")
    public BaseResponse<Long> createUser(@RequestParam final String userAccount, final HttpServletRequest request) {
        return ResponseUtils.success(userService.createUser(userAccount, request));
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody final UserUpdateRequest request) {
        if (request == null) {
            throw new CustomException(ErrorCode.PARAMS_ERROR);
        }

        final User user = User.builder()
                .id(request.getId())
                .username(request.getUsername())
                .userAccount(request.getUserAccount())
                .avatarUrl(request.getAvatarUrl())
                .gender(request.getGender())
                .userPassword(request.getUserPassword())
                .phone(request.getPhone())
                .email(request.getEmail())
                .updateTime(new Date())
                .build();

        return ResponseUtils.success(userService.updateUser(user));
    }
}
