package com.yuejiangw.usercenterbackend.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateRequest implements Serializable {

    private long id;

    private String username;

    private String userAccount;

    private String avatarUrl;

    private int gender;

    private String userPassword;

    private String phone;

    private String email;
}
