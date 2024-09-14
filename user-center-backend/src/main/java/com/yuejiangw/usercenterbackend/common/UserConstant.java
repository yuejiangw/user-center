package com.yuejiangw.usercenterbackend.common;

import java.util.List;

public interface UserConstant {
    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "password";

    String MESSAGE_OK = "ok";

    int CODE_OK = 0;

    String ID = "id";

    String USERNAME = "username";

    String USER_ACCOUNT = "userAccount";

    String GENDER = "gender";

    String PHONE = "phone";

    String EMAIL = "email";

    String USER_ROLE = "userRole";

    List<String> EXACT_MATCH = List.of(ID, GENDER, USER_ROLE);

    List<String> PATTERN_MATCH = List.of(USERNAME, USER_ACCOUNT, PHONE, EMAIL);
}
