package com.yuejiangw.usercenterbackend.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Lesson implements Serializable {
    @Serial
    private static final long serialVersionUID = 1287394968777080282L;

    private String content;

    private String courseTime;
}
