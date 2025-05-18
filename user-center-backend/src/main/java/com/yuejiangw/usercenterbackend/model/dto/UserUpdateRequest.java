package com.yuejiangw.usercenterbackend.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -8243712393318697464L;

    private long id;

    private String username;

    private String userAccount;

    private String avatarUrl;

    private int gender;

    private String userPassword;

    private String phone;

    private String email;
}
