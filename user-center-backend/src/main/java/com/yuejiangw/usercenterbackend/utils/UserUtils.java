package com.yuejiangw.usercenterbackend.utils;

import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.exception.BusinessException;
import com.yuejiangw.usercenterbackend.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yuejiangw.usercenterbackend.common.constants.UserConstant.ADMIN_ROLE;
import static com.yuejiangw.usercenterbackend.common.constants.UserConstant.USER_LOGIN_STATE;

public class UserUtils {

    private UserUtils() {
    }

    public static void checkNull(String... args) throws BusinessException {
        if (StringUtils.isAnyBlank(args)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "user input can not be null");
        }
    }

    public static void checkLength(String userAccount, String... userPassword) throws BusinessException {
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account's length should >= 4");
        }
        for (String password : userPassword) {
            if (password.length() < 8) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Password's length should >= 8");
            }
        }

    }

    public static void checkValidCharacter(String userAccount) throws BusinessException {
        String validPattern = "^[a-zA-Z0-9]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,
                    "You can only use digits and English letters to create your account");
        }
    }

    public static boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    public static User getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        final User user = (User) userObj;
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "You need to login first");
        }
        return user;
    }
}
