package com.yuejiangw.usercenterbackend.utils;

import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.exception.CustomException;
import com.yuejiangw.usercenterbackend.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import static com.yuejiangw.usercenterbackend.common.constants.UserConstant.ADMIN_ROLE;
import static com.yuejiangw.usercenterbackend.common.constants.UserConstant.USER_LOGIN_STATE;

public class UserUtils {

    private UserUtils() {}

    public static boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    public static User getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        final User user = (User) userObj;
        if (user == null) {
            throw new CustomException(ErrorCode.NOT_LOGIN, "You need to login first");
        }
        return user;
    }
}
