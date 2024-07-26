package com.yuejiangw.usercenterbackend.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6656877786838073351L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
