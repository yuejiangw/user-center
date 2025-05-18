package com.yuejiangw.usercenterbackend.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6922018361419643748L;

    private String userAccount;

    private String userPassword;
}
